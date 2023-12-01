package org.example.orm.binding;

import org.example.orm.session.Configuration;
import org.example.orm.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    private final Configuration configuration;
    private final Map<Class<?>, MapperProxyFactory> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    // TODO 添加mapper
    public <T> void addMapper(Class<T> type) {
        if(type.isInterface()) {

        }
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return (T)new MapperProxyFactory(type).newInstance(sqlSession);
    }
}
