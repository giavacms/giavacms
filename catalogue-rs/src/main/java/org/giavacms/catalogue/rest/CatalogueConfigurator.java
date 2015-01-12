package org.giavacms.catalogue.rest;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.giavacms.catalogue.model.Product;
import org.jboss.logging.Logger;

@Singleton
@Startup
public class CatalogueConfigurator
{

   Logger logger = Logger.getLogger(getClass());
   @Inject
   ObjectMapper objectMapper;

   public CatalogueConfigurator()
   {
   }

   @PostConstruct
   public void init()
   {
      logger.info("add module");

      logger.info("objectMapper is " + objectMapper);

      objectMapper.getSerializationConfig()
               .addMixInAnnotations(Product.class, ProductMixin.class);

   }
}
