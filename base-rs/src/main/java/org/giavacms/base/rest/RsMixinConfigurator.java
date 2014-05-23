package org.giavacms.base.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.giavacms.base.rest.producer.ObjectMapperProducer;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

@Provider
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public class RsMixinConfigurator extends ResteasyJacksonProvider {

	Logger logger = Logger.getLogger(RsMixinConfigurator.class.getName());

	public RsMixinConfigurator() {
		super();
		logger.info("RsMixinConfigurator startup " + this);

		_mapperConfig.setMapper(new ObjectMapperProducer().getObjectMapper());
		
		// configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
		// false);
		// configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

	}

}
