package org.giavacms.base.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.giavacms.base.rest.producer.ObjectMapperProducer;

@Provider
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public class RsMixinResolver implements ContextResolver<ObjectMapper>
{

   private ObjectMapper _objectMapper;

   Logger logger = Logger.getLogger(RsMixinResolver.class.getName());

   public RsMixinResolver()
   {
      super();
      logger.info("RsMixinResolver startup " + this);

      _objectMapper = new ObjectMapperProducer().getObjectMapper();

      logger.info("objectMapper is " + _objectMapper);

      _objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
   }

   public ObjectMapper getContext(Class<?> objectType)
   {
      logger.info("getContext() --> objectMapper is " + _objectMapper);
      return _objectMapper;
   }

}
