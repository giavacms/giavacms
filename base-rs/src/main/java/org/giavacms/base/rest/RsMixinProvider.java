package org.giavacms.base.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.giavacms.base.rest.producer.ObjectMapperProducer;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

//@Provider
//@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
//@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public class RsMixinProvider extends ResteasyJacksonProvider
{

   Logger logger = Logger.getLogger(RsMixinProvider.class.getName());

   public RsMixinProvider()
   {
      super();
      logger.info("RsMixinConfigurator startup " + this);

      ObjectMapper objectMapper = new ObjectMapperProducer().getObjectMapper();

      logger.info("objectMapper is " + objectMapper);

      _mapperConfig.setMapper(objectMapper);

      // configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
      // false);
      // configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

   }

}
