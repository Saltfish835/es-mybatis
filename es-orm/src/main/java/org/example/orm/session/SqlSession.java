package org.example.orm.session;

import java.util.List;

public interface SqlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T> List<T> selectList(String statement);

    <T> List<T> selectList(String statement, Object parameter);

    <T> T getMapper(Class<T> type);

    void close();
}
