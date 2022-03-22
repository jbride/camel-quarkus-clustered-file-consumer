package com.redhat.himss;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.runtime.StartupEvent;
import org.apache.camel.CamelContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CamelResource {

    private static Logger log = Logger.getLogger(CamelResource.class);

    @Inject
    @DataSource("camel-ds")
    AgroalDataSource dataSource;

    void startup(@Observes StartupEvent event, CamelContext context) throws Exception {
        context.getRouteController().startAllRoutes();
    }

    @PostConstruct
    void postConstruct() throws Exception {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(true);
                Statement statement = con.createStatement();
                statement.execute("select count(messageId) from camel_messageprocessed;");
                ResultSet rSet = statement.getResultSet();
                rSet.next();
                int count = rSet.getInt(1);
                log.infov("# of records in {0} = {1}", "camel_messageprocessed", count);
        }
    }
}
