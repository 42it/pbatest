package com.paintingbuddha.video;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

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
}
