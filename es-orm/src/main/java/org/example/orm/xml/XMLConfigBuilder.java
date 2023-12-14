package org.example.orm.xml;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.orm.session.Configuration;
import org.example.orm.session.IgnoreDTDEntityResolver;
import org.example.orm.session.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析Mapper.xml文件
 * 记录下<全限定名.方法名，xnode>
 * 注册<mapper接口，mapper代理对象工厂>
 * @author yuhe
 */
public class XMLConfigBuilder {

    private Logger logger = LoggerFactory.getLogger(XMLConfigBuilder.class);

    private String basePackage;

    private Configuration configuration;

    public XMLConfigBuilder(String basePackage, Configuration configuration) {
        this.basePackage = basePackage;
        this.configuration = configuration;
    }

    public void parse() {
        String resourcePath = this.getClass().getClassLoader().getResource("").getPath();
        File basePackage = new File(resourcePath + this.basePackage);
        if(!basePackage.isDirectory()) {
            throw new RuntimeException("path " +basePackage+ "is not a directory");
        }

        Map<String, XNode> mappedStatements = new HashMap<>();
        File[] mappers = basePackage.listFiles();
        for(File mapper : mappers) {
            this.getMapperElement(mapper,mappedStatements);
        }
        this.configuration.setMappedStatements(mappedStatements);
    }


    /**
     * 解析一份mapper文件，
     * @param mapper
     * @param mappedStatements
     */
    private void getMapperElement(File mapper, Map<String, XNode> mappedStatements) {
        try {
            SAXReader saxReader = new SAXReader(false);
            saxReader.setEntityResolver(new IgnoreDTDEntityResolver());
            Document document = saxReader.read(mapper);
            Element root = document.getRootElement();
            // 获取命名空间（接口的全限定类名）
            String namespace = root.attributeValue("namespace");
            // 注册mapper接口代理工厂
            this.configuration.addMapper(Class.forName(namespace));
            // 获取所有select标签
            List<Element> selectNodes = root.selectNodes("select");
            // 遍历所有select标签
            for (Element selectNode : selectNodes) {
                String id = selectNode.attributeValue("id");
                String parameterType = selectNode.attributeValue("parameterType");
                String resultType = selectNode.attributeValue("resultType");
                String sql = selectNode.getText();
                // 匹配sql中的#{}，换成占位符？
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }
                // 封装标签
                XNode xNode = new XNode();
                xNode.setNamespace(namespace);
                xNode.setId(id);
                xNode.setParameterType(parameterType);
                xNode.setResultType(resultType);
                xNode.setSql(sql);
                xNode.setParameter(parameter);
                // 把标签存起来
                mappedStatements.put(namespace + "." + id, xNode);
            }
        }catch (Exception e) {
            logger.error("解析xml文件失败",e);
        }
    }

}
