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
        MapperProxyFactory<T> mapperProxyFactory = this.knownMappers.get(type);
        if(mapperProxyFactory == null) {
            throw new RuntimeException("Type " +type+ " is not known to the MapperRegistry");
        }else {
            try{
                return mapperProxyFactory.newInstance(sqlSession);
            }catch (Exception e) {
                throw new RuntimeException("Error getting mapper instance. Cause: "+e, e);
            }
        }
    }


    /**
     * 添加Mapper接口对应的工厂
     * @param type
     * @param <T>
     */
    public <T> void addMapper(Class<T> type) {
        if(type.isInterface()) {
            if(this.hasMapper(type)) {
                throw new RuntimeException("Type "+ type +" is already known to the MapperRegistry.");
            }
            this.knownMappers.put(type, new MapperProxyFactory(type));
        }
    }

}
