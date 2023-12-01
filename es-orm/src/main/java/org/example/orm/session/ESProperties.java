package org.example.orm.session;

public class ESProperties {
    /**
     * jdbc url
     */
    private String jdbcUrl;

    /**
     * Mapper接口路径
     */
    private String basePackage;

    /**
     * Mapper文件路径
     */
    private String mappersPath;

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

    public String getMappersPath() {
        return mappersPath;
    }

    public void setMappersPath(String mappersPath) {
        this.mappersPath = mappersPath;
    }
}
