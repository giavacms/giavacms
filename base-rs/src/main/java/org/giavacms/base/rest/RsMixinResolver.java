package org.giavacms.base.rest;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.giavacms.base.rest.producer.ObjectMapperProducer;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class RsMixinResolver implements ContextResolver<ObjectMapper>
{

   private ObjectMapper _objectMapper;

   public RsMixinResolver()
   {
      _objectMapper = new ObjectMapperProducer().getObjectMapper();
      _objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
   }

   public ObjectMapper getContext(Class<?> objectType)
   {
      return _objectMapper;
   }

}
