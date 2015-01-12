package org.giavacms.base.rest.producer;

import javax.enterprise.inject.Produces;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperProducer
{

   static ObjectMapper objectMapper = new ObjectMapper();

   @Produces
   public ObjectMapper getObjectMapper()
   {
      return objectMapper;
   }

}
