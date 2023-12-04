package org.example.orm.binding;

import org.example.orm.session.Configuration;
import org.example.orm.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    private final Configuration configuration;

    /**
     * 缓存MapperProxyFactory对象
     */
    private final Map<Class<?>, MapperProxyFactory> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }


    /**
     * 判断缓存中是否有指定类型的MapperProxyFactory对象
     * @param type
     * @param <T>
     * @return
     */
    public <T> boolean hasMapper(Class<T> type) {
        return this.knownMappers.containsKey(type);
    }


    /**
     * 获取Mapper接口的代理对象
     * @param type
     * @param sqlSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        if(type.isInterface()) {
            if(this.hasMapper(type)) {
                MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory)this.knownMappers.get(type);
                return mapperProxyFactory.newInstance(sqlSession);
            }else {
                MapperProxyFactory mapperProxyFactory = new MapperProxyFactory(type);
                this.knownMappers.put(type, mapperProxyFactory);
                return (T)mapperProxyFactory.newInstance(sqlSession);
            }
        }
        return null;
    }
}
