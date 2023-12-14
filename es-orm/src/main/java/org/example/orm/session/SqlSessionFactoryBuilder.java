package org.example.orm.session;

import org.example.orm.session.defaults.DefaultSqlSessionFactory;
import org.example.orm.xml.XMLConfigBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 用于创建SqlSessionFactory
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactoryBuilder() {

    }

    /**
     * 创建SqlSessionFactory
     * @param esProperties
     * @return
     */
    public SqlSessionFactory build(ESProperties esProperties) {

        Configuration configuration = new Configuration();
        // 根据配置文件产生connection对象
        configuration.setConnection(getConnection(esProperties));
        // 解析xml文件，生成mapper接口代理工厂及XNode保存到configuration对象中
        XMLConfigBuilder mapperParser = new XMLConfigBuilder(esProperties.getBasePackage(), configuration);
        mapperParser.parse();
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
            throw new RuntimeException(e);
        }
    }
}
