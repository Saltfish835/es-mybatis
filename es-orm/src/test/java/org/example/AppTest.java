package org.example;


import org.example.bean.User;
import org.example.mapper.BookMapper;
import org.example.mapper.UserMapper;
import org.example.orm.session.ESProperties;
import org.example.orm.session.SqlSession;
import org.example.orm.session.SqlSessionFactory;
import org.example.orm.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    private String jdbcUrl = "jdbc:es://http://127.0.0.1:9200";
    private String mappersPath = "mapper/UserMapper.xml";
    private ESProperties esProperties;

    @Before
    public void getSqlSession() {
        this.esProperties = new ESProperties();
        esProperties.setJdbcUrl(jdbcUrl);
        esProperties.setMappersPath(mappersPath);
    }

    @Test
    public void testSelectOne() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object user = sqlSession.selectOne("org.example.mapper.UserMapper.queryUser");
        System.out.println(user);
    }

    @Test
    public void testSelectOneByName() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object user = sqlSession.selectOne("org.example.mapper.UserMapper.queryUserByName", "王五");
        System.out.println(user);
    }

    @Test
    public void testSelectList() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("org.example.mapper.UserMapper.queryUsers");
        System.out.println(objects);
    }

    @Test
    public void testSelectListByParameter() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setGender("male");
        List<Object> objects = sqlSession.selectList("org.example.mapper.UserMapper.query", user);
        System.out.println(objects);
    }

    @Test
    public void testSelectOneGetMapper() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.queryUser();
        System.out.println(user);
    }

    @Test
    public void testSelectListGetMapper() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setGender("male");
        List<User> userList = userMapper.query(user);
        System.out.println(userList);
    }

    @Test
    public void testObjectMethod() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        String s = userMapper.toString();
        System.out.println(s);
    }


    @Test
    public void testNotExistMethod() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(esProperties);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
        String s = bookMapper.toString();
        System.out.println(s);
    }


}
