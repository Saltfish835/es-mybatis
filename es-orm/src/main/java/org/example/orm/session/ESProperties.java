package org.example.orm.session;

public class ESProperties {
    /**
     * jdbc url
     */
    private String jdbcUrl;

    /**
     * Mapper文件所在目录
     */
    private String basePackage;


    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

}
