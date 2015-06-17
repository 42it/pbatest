package com.paintingbuddha.video;

import java.net.URI;
import java.util.Properties;

import javax.sql.DataSource;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.ResourceTransactionManager;

@org.springframework.context.annotation.Configuration
@EnableAutoConfiguration
public class Configuration
{
    @Bean
    AmazonS3 s3Client()
    {
        AWSCredentials credentials = new EnvironmentVariableCredentialsProvider().getCredentials();
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        return s3;
    }
    
    @Bean
    DataSource provideDataSource()
    {
        try
        {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            
            Properties properties = new Properties();
            properties.put("driverClassName", "org.postgresql.Driver");
            properties.put("url", dbUrl);
            properties.put("username", username);
            properties.put("password", password);
            return BasicDataSourceFactory.createDataSource(properties);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to create datasource", e);
        }
    }
    
    @Bean
    JdbcTemplate provideJdbcTemplate(DataSource dataSource)
    {
        try
        {
            return new JdbcTemplate(dataSource);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to create JDBC Template", e);
        }
    }

    @Bean
    ResourceTransactionManager provideDataSourceTransactionManager(DataSource dataSource)
    {
        try
        {
            return new DataSourceTransactionManager(dataSource);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to create datasource transaction manager", e);
        }
    }

}
