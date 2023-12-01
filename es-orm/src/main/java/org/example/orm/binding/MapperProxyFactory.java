package org.example.orm.binding;

import org.example.orm.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

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
     * 创建Mapper接口的代理对象
     * @param sqlSession
     * @return
     */
    public T newInstance(SqlSession sqlSession) {

        Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{this.mapperInterface},
                // 调用mapper接口的方法，最终都会走到这个方法中
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName = method.getName();
                        // Object类中继承下来的方法不代理
                        Method[] methods = Object.class.getMethods();
                        for (Method objMethod : methods) {
                            if (objMethod.getName().equals(methodName)) {
                                return null;
                            }
                        }
                        // 实际最终是SqlSession来执行方法
                        // 通过返回值类型判断最终要执行什么方法
                        String statement = mapperInterface.getName() +"."+ methodName;
                        if(method.getReturnType() == List.class) {
                            return sqlSession.selectList(statement, args[0]);
                        }else {
                            return sqlSession.selectOne(statement, args[0]);
                        }
                    }
                });
        return (T)proxyInstance;
    }
}
