package org.example.orm.binding;

import org.example.orm.session.SqlSession;
import java.lang.reflect.Proxy;

/**
 * 用于产生Mapper接口的代理类
 * @param <T>
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }


    /**
     * 真正调用jdk Proxy类来生成代理对象
     * @param mapperProxy
     * @return
     */
    protected T newInstance(MapperProxy<T> mapperProxy) {
        // 通过jdk的Proxy类创建接口的代理对象
        return (T)Proxy.newProxyInstance(this.mapperInterface.getClassLoader(),new Class[]{this.mapperInterface}, mapperProxy);
    }


    /**
     * 创建Mapper接口的代理对象
     * @param sqlSession
     * @return
     */
    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy(sqlSession, this.mapperInterface);
        return this.newInstance(mapperProxy);
    }
}
