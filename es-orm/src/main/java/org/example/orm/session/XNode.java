package org.example.orm.session;

import java.util.Map;

/**
 * 封装xml标签
 */
public class XNode {
    /**
     * 对应Mapper接口的全限定名
     */
    private String namespace;

    /**
     * 对应Mapper接口中的方法
     */
    private String id;

    /**
     * 方法的参数类型
     */
    private String parameterType;

    /**
     * 方法的返回值类型
     */
    private String resultType;

    /**
     * 标签中定义的sql
     */
    private String sql;

    /**
     * 实际传递的参数
     */
    private Map<Integer, String> parameter;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "XNode{" +
                "namespace='" + namespace + '\'' +
                ", id='" + id + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", resultType='" + resultType + '\'' +
                ", sql='" + sql + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
