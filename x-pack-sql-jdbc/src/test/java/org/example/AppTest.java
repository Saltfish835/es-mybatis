package org.example;


import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void execSql()
    {
        String address = "jdbc:es://http://127.0.0.1:9200/";
        Properties connectionProperties = new Properties();
        try {
            Connection connection = DriverManager.getConnection(address, connectionProperties);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT name FROM tppa_test");
            while (results.next()) {
                System.out.println(results.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
