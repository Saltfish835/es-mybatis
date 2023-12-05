package org.example.mybatis.spring;

import org.example.orm.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 创建Mapper接口的代理类,并向Spring中注入Mapper接口的代理类
 * @author yuhe
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;
    private SqlSessionFactory sqlSessionFactory;

    public MapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public T getObject() throws Exception {
        return this.sqlSessionFactory.openSession().getMapper(this.mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return this.mapperInterface;
    }
}
