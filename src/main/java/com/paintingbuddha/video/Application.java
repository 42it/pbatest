package com.paintingbuddha.video;

import com.paintingbuddha.video.controller.UserHandlerInterceptor;
import com.paintingbuddha.video.filter.HullUserFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("home");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new UserHandlerInterceptor());
    }
    
    @org.springframework.context.annotation.Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter
    {
        @Autowired
        private SecurityProperties security;
        @Autowired
        private HullUserFilter filter;

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/images/**").anonymous()
                .antMatchers("/all").hasRole("ADMIN")
                .antMatchers("/upload").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(filter, AbstractPreAuthenticatedProcessingFilter.class);
            http.csrf().disable();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception
        {
        }

    }
}
