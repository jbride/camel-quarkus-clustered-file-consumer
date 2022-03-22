package com.redhat.na;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.infinispan.InfinispanIdempotentRepository;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
public class Routes extends RouteBuilder {

    private static Logger log = Logger.getLogger(Routes.class);

    @Inject
    private InfinispanIdempotentRepository inProgressRepo;
    
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
        from("file:{{com.redhat.na.file.input.location}}?inProgressRepository=#inProgressRepo&delay=1000&autoCreate=true&delete=false")
        //from("file:{{com.redhat.na.file.input.location}}?delay=1000&autoCreate=true&delete=false")
            .routeId("direct:fileConsumer")
            .log("file = ${header.CamelFileName}}")
            .end();

    }
}
