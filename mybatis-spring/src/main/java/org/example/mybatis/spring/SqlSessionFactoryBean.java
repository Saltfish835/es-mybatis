package org.example.mybatis.spring;

import org.example.orm.session.ESProperties;
import org.example.orm.session.SqlSessionFactory;
import org.example.orm.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 用于创建SqlSessionFactory
 * @author yuhe
 */
public class SqlSessionFactoryBean implements FactoryBean, InitializingBean {


    private ESProperties esProperties;
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 配置信息需要在Spring配置文件中注入进来
     * @param esProperties
     */
    public void setEsProperties(ESProperties esProperties) {
        this.esProperties = esProperties;
    }

    @Override
    public Object getObject() throws Exception {
        if(this.sqlSessionFactory == null) {
            this.afterPropertiesSet();
        }
        return this.sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    /**
     * 创建SqlSessionFactory对象
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
    }
}
