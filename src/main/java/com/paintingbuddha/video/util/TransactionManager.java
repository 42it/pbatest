package com.paintingbuddha.video.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.ResourceTransactionManager;

@Component
public class TransactionManager implements MethodInterceptor
{
    private ResourceTransactionManager dataSourceTransactionManager;

    @Autowired
    public void setDataSourceTransactionManager(ResourceTransactionManager dataSourceTransactionManager)
    {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {

        if (dataSourceTransactionManager == null)
        {
            throw new IllegalStateException("No datasource transaction manager set for transaction manager interceptor");
        }

        TransactionStatus transaction =
            dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_SUPPORTS));

        try
        {
            return invocation.proceed();
        }
        catch (Throwable e)
        {
            transaction.setRollbackOnly();
            dataSourceTransactionManager.rollback(transaction);
            throw e;
        }
        finally
        {
            if (!transaction.isRollbackOnly())
            {
                dataSourceTransactionManager.commit(transaction);
            }
        }
    }
}
