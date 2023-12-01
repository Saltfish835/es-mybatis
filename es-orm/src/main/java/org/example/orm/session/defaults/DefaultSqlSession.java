package org.example.orm.session.defaults;

import lombok.extern.slf4j.Slf4j;
import org.example.orm.session.Configuration;
import org.example.orm.session.SqlSession;
import org.example.orm.session.XNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
public class DefaultSqlSession implements SqlSession {

    /**
     * 配置类
     */
    private Configuration configuration;

    /**
     * 操作数据库的对象
     */
    private Connection connection;

    /**
     * mapper文件解析结果
     */
    private Map<String, XNode> mapperElement;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.connection = configuration.getConnection();
        this.mapperElement = configuration.getMapperElement();
    }

    /**
     * 查询单条数据
     * @param statement mapper的全限定性类名.方法
     * @param <T>
     * @return
     */
    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement,null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        XNode xNode = this.mapperElement.get(statement);
        Map<Integer, String> parameterMap = xNode.getParameter();
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(xNode.getSql());
            // 将sql与参数值使用参数位置组装起来
            this.buildParameter(preparedStatement, parameter, parameterMap);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> objects = this.resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
            return objects.get(0);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statement) {

        return this.selectList(statement, null);
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        XNode xNode = this.mapperElement.get(statement);
        Map<Integer, String> parameterMap = xNode.getParameter();
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(xNode.getSql());
            this.buildParameter(preparedStatement,parameter,parameterMap);
            ResultSet resultSet = preparedStatement.executeQuery();
            return this.resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return this.configuration.getMapper(type,this);
    }

    @Override
    public void close() {
        if(null == connection) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将结果集封装成对象
     * @param resultSet
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 结果集中有多少列
            int columnCount = metaData.getColumnCount();
            // 遍历行值
            while(resultSet.next()) {
                // 遍历列值，将值封装到obj对象中去
                T obj = (T) clazz.newInstance();
                for(int i=1;i<=columnCount;i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod = "set"+columnName.substring(0,1).toUpperCase()+columnName.substring(1);
                    Method method;
                    if(value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    }else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj,value);
                }
                list.add(obj);
            }
        }catch (Exception e) {
            log.error("execute resultSet2Obj error",e);
        }
        return list;
    }


    /**
     * 将sql中的占位符换成对应参数值
     * @param preparedStatement
     * @param parameter
     * @param parameterMap
     * @throws SQLException
     * @throws IllegalAccessException
     */
    private void buildParameter(PreparedStatement preparedStatement, Object parameter, Map<Integer, String> parameterMap) throws SQLException, IllegalAccessException {
        if(parameter == null) {
            return;
        }
        int size = parameterMap.size();

        // 当前参数是基础类型
        if(parameter instanceof Long) {
            for(int i=1;i<=size;i++) {
                preparedStatement.setLong(i, Long.parseLong(parameter.toString()));
            }
            return;
        }
        if(parameter instanceof Integer) {
            for(int i=1;i<=size;i++) {
                preparedStatement.setInt(i, Integer.parseInt(parameter.toString()));
            }
            return;
        }
        if(parameter instanceof String) {
            for(int i=1;i<=size;i++) {
                preparedStatement.setString(i, parameter.toString());
            }
            return;
        }

        // 当前参数是对象类型
        Map<String, Object> fieldMap = new HashMap<>();
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        for(Field field: declaredFields) {
            String fieldName = field.getName();
            field.setAccessible(true);
            // 拿到parameter对象此字段的值
            Object obj = field.get(parameter);
            field.setAccessible(false);
            fieldMap.put(fieldName,obj);
        }
        for(int i=1;i<=size;i++) {
            // 拿到sql中第i个参数的名称
            String parameterDefine = parameterMap.get(i);
            // 使用名称拿到参数的值
            Object obj = fieldMap.get(parameterDefine);
            if(obj instanceof Short) {
                preparedStatement.setShort(i,Short.parseShort(obj.toString()));
                continue;
            }
            if(obj instanceof Integer) {
                preparedStatement.setInt(i,Integer.parseInt(obj.toString()));
                continue;
            }
            if(obj instanceof Long) {
                preparedStatement.setLong(i,Long.parseLong(obj.toString()));
                continue;
            }
            if(obj instanceof String) {
                preparedStatement.setString(i,obj.toString());
                continue;
            }
            if(obj instanceof Date) {
                preparedStatement.setDate(i,(java.sql.Date) obj);
            }
        }
    }
}
