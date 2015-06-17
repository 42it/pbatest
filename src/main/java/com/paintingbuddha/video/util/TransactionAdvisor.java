package com.paintingbuddha.video.util;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionAdvisor extends AbstractPointcutAdvisor
{
    public static final int serialVersionUID = -1;
    
    @Autowired
    private transient TransactionManager interceptor;

    private final transient StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut()
    {
        @Override
        public boolean matches(Method method, Class<?> targetClass)
        {
            return method.isAnnotationPresent(Transactional.class);
        }
    };

    @Override
    public Pointcut getPointcut()
    {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice()
    {
        return this.interceptor;
    }
}
