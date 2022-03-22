package com.redhat.himss;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.idempotent.jdbc.JdbcOrphanLockAwareIdempotentRepository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SqlOrphanLockAwareBean {

    private static Logger log = Logger.getLogger(SqlOrphanLockAwareBean.class);

    @Inject
    @DataSource("camel-ds")
    AgroalDataSource dataSource;

    public SqlOrphanLockAwareBean() {

        /*
          Doesn't seem to work in quarkus

          Caused by: java.lang.IllegalArgumentException: Property 'dataSource' is required
	        at org.springframework.jdbc.datasource.DataSourceTransactionManager.afterPropertiesSet(DataSourceTransactionManager.java:231)
	        at org.springframework.jdbc.datasource.DataSourceTransactionManager.<init>(DataSourceTransactionManager.java:142)
	        at org.apache.camel.processor.idempotent.jdbc.AbstractJdbcMessageIdRepository.createTransactionTemplate(AbstractJdbcMessageIdRepository.java:116)
	        at org.apache.camel.processor.idempotent.jdbc.AbstractJdbcMessageIdRepository.<init>(AbstractJdbcMessageIdRepository.java:77)
	        at org.apache.camel.processor.idempotent.jdbc.JdbcMessageIdRepository.<init>(JdbcMessageIdRepository.java:51)
	        at org.apache.camel.processor.idempotent.jdbc.JdbcOrphanLockAwareIdempotentRepository.<init>(JdbcOrphanLockAwareIdempotentRepository.java:76)
	        at com.redhat.himss.SqlOrphanLockAwareBean.<init>(SqlOrphanLockAwareBean.java:24)
	        at com.redhat.himss.SqlOrphanLockAwareBean_ClientProxy.<init>(Unknown Source)
	        at com.redhat.himss.SqlOrphanLockAwareBean_Bean.proxy(Unknown Source)
	        at com.redhat.himss.SqlOrphanLockAwareBean_Bean.get(Unknown Source)
	        at com.redhat.himss.SqlOrphanLockAwareBean_Bean.get(Unknown Source)

         */
        JdbcOrphanLockAwareIdempotentRepository repo = new JdbcOrphanLockAwareIdempotentRepository(dataSource, "APP_1", new DefaultCamelContext());
    }

}
