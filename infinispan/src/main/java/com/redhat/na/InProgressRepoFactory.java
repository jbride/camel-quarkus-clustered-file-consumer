package com.redhat.na;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.component.infinispan.remote.InfinispanRemoteIdempotentRepository;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InProgressRepoFactory {

    private static Logger log = Logger.getLogger(InProgressRepoFactory.class);

    private static final String CACHE_NAME="fileInProgressRepo";
	private static final String inProgressRepoCacheConfig =
    "<infinispan><cache-container>" +
	"<distributed-cache name=\"%s\"></distributed-cache>" +
	"</cache-container></infinispan>";

    @Inject
	RemoteCacheManager cacheManager;

    InfinispanRemoteIdempotentRepository repo;

    private void createRepo() {
        String xml = String.format(inProgressRepoCacheConfig, CACHE_NAME);
		RemoteCache<Object, Object> rCache = cacheManager.administration().getOrCreateCache(CACHE_NAME, new XMLStringConfiguration(xml));
		String cacheName = rCache.getName();
		log.info("createRepo() InfinispanInProgressRepoBean() cacheName = "+cacheName);
    }
    
    
    /*
    Caused by: java.lang.NullPointerException
	    at org.infinispan.client.hotrod.configuration.ConfigurationBuilder.parseServers(ConfigurationBuilder.java:131)
	    at org.infinispan.client.hotrod.configuration.ConfigurationBuilder.addServers(ConfigurationBuilder.java:122)
	    at org.apache.camel.component.infinispan.remote.InfinispanRemoteManager.doStart(InfinispanRemoteManager.java:80)
	    at org.apache.camel.support.service.BaseService.start(BaseService.java:119)
	    at org.apache.camel.component.infinispan.remote.InfinispanRemoteIdempotentRepository.doStart(InfinispanRemoteIdempotentRepository.java:54)
	    at org.apache.camel.support.service.BaseService.start(BaseService.java:119)
	    at com.redhat.na.InProgressRepoFactory_ProducerMethod_getInProgressRepo_2d5aceedcfb91c28aa3e78f81dcedbbb8d59bcf1_ClientProxy.start(Unknown Source)
     */
    @Produces
    @Named("inProgressRepo")
    @ApplicationScoped
    public InfinispanRemoteIdempotentRepository getInProgressRepo() {
        if(repo == null)
          createRepo();
        return new InfinispanRemoteIdempotentRepository(CACHE_NAME);
    }
    
}
