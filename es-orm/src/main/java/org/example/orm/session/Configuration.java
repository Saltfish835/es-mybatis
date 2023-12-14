package org.example.orm.session;

import org.example.orm.binding.MapperRegistry;
import org.example.orm.xml.XNode;

import java.sql.Connection;
import java.util.Map;

/**
 * 封装ES使用xml文件操作数据库所需的配置
 */
public class Configuration {

    /**
     * 操作数据的对象
     */
    protected Connection connection;

    /**
     * 数据源配置
     */
    protected Map<String, String> dataSource;

    /**
     * xml文件解析结果
     * 全限定类名.方法 ----> xml节点（包含sql、参数类型、返回值类型）
     */
    protected Map<String, XNode>  mappedStatements;

    /**
     * 存取Mapper代理对象
     */
    protected final MapperRegistry mapperRegistry;

    public Configuration() {
        this.mapperRegistry = new MapperRegistry(this);
    }

    public Map<String, XNode> getMappedStatements() {
        return mappedStatements;
    }

    public void setMappedStatements(Map<String, XNode> mappedStatements) {
        this.mappedStatements = mappedStatements;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public Map<String, String> getDataSource() {
        return dataSource;
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(type, sqlSession);
    }

    public <T> void addMapper(Class<T> type) {
        this.mapperRegistry.addMapper(type);
    }

}
