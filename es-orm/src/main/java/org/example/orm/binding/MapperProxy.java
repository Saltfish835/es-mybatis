package org.example.orm.binding;

import org.example.orm.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 定义Mapper接口的代理类对象在调用方法时执行的逻辑
 * @author yuhe
 */
public class MapperProxy<T> implements InvocationHandler {

    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;


    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }


    /**
     * 调用代理类的方法最终都会调用这个invoke方法
     * @param proxy 代理类对象
     * @param method 当前执行的方法
     * @param args 当前执行的方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object类中继承下来的方法不代理
        if(Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        // 实际最终是SqlSession来执行方法
        String statement = mapperInterface.getName() +"."+ method.getName();
        // 通过返回值类型判断最终要执行什么方法
        if(method.getReturnType() == List.class) {
            return sqlSession.selectList(statement, args[0]);
        }else {
            return sqlSession.selectOne(statement, args[0]);
        }
    }
}
