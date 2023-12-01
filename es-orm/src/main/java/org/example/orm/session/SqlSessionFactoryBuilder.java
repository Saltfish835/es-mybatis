package org.example.orm.session;

import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.orm.session.defaults.DefaultSqlSessionFactory;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于创建SqlSessionFactory
 */
@Slf4j
public class SqlSessionFactoryBuilder {

    public SqlSessionFactoryBuilder() {

    }

    /**
     * 创建SqlSessionFactory
     * @param esProperties
     * @return
     */
    public SqlSessionFactory build(ESProperties esProperties) {
        // 拿到配置文件后解析出配置
        Configuration configuration = new Configuration();

        // 根据配置文件产生connection对象
        configuration.setConnection(getConnection(esProperties));
        // 根据配置文件将xml文件解析成<类名.方法名,xnode>的形式
        configuration.setMapperElement(getMapperElement(esProperties.getMappersPath()));

        // 创建SqlSessionFactory对象
        return new DefaultSqlSessionFactory(configuration);
    }


    /**
     * 获取Connection连接
     * @param esProperties
     * @return
     */
    private Connection getConnection(ESProperties esProperties) {
        try{
            Connection connection = DriverManager.getConnection(esProperties.getJdbcUrl(), new Properties());
            return connection;
        }catch (SQLException e) {
            log.error("Create conn error",e);
            throw new RuntimeException();
        }
    }


    /**
     * 解析Mapper文件
     * 当前只能解析一份文件？
     * @param mappersPath
     * @return
     */
    private Map<String, XNode> getMapperElement(String mappersPath) {
        Map<String, XNode> map = new HashMap<>();
        try {
            Reader reader = Resources.getResourceAsReader(mappersPath);
            SAXReader saxReader = new SAXReader(false);
            saxReader.setEntityResolver(new IgnoreDTDEntityResolver());
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();
            // 获取命名空间
            String namespace = root.attributeValue("namespace");

            // 获取到所有select标签
            List<Element> selectNodes = root.selectNodes("select");
            // 遍历所有select标签
            for(Element selectNode : selectNodes) {
                String id = selectNode.attributeValue("id");
                String parameterType = selectNode.attributeValue("parameterType");
                String resultType = selectNode.attributeValue("resultType");
                String sql = selectNode.getText();
                // 匹配sql中的#{}，换成占位符？
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }

                // 封装标签
                XNode xNode = new XNode();
                xNode.setNamespace(namespace);
                xNode.setId(id);
                xNode.setParameterType(parameterType);
                xNode.setResultType(resultType);
                xNode.setSql(sql);
                xNode.setParameter(parameter);

                // 把标签存起来
                map.put(namespace +"."+ id, xNode);
            }
        }catch (Exception e) {
            log.error("parse "+mappersPath+" error",e);
        }
        return map;
    }
}
