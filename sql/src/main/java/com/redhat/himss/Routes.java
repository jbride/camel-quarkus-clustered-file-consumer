package com.redhat.himss;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Camel route definitions.
 */
@ApplicationScoped
public class Routes extends RouteBuilder {

    private static Logger log = Logger.getLogger(Routes.class);  
    
    @Inject
    SqlOrphanLockAwareBean sqlOrphanLockAwareBean;
    
    public Routes() {
    }

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        /*****                Consume from HTTP           *************/
        rest("/sanityCheck")
                .get()
                .route()
                .setBody().constant("Good To Go!")
                .endRest();


        /*****                Consume from filesystem           *************/
        from("file:{{com.redhat.na.file.input.location}}?inProgressRepository=#sqlOrphanLockAwareBean&delay=1000&autoCreate=true&delete=false")
            .routeId("direct:fileConsumer")
            .log("file = ${header.CamelFileName}}")
            .end();


    }

    class CSVPayloadValidator implements Processor {

        @Override
        public void process(Exchange exchange) throws ValidationException {

        }
    }
}
