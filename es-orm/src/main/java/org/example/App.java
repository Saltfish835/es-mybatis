package org.example;



import org.example.orm.session.ESProperties;
import org.example.orm.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws FileNotFoundException {
        String jdbcUrl = "jdbc:es://http://127.0.0.1:9200";
        String mappers = "mapper/UserMapper.xml";

        ESProperties esProperties = new ESProperties();
        esProperties.setJdbcUrl(jdbcUrl);
        esProperties.setMappersPath(mappers);



    }
}
