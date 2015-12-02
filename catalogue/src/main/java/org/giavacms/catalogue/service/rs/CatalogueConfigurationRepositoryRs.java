package org.giavacms.catalogue.service.rs;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.catalogue.management.AppConstants;
import org.giavacms.catalogue.model.CatalogueConfiguration;
import org.giavacms.catalogue.repository.CatalogueConfigurationRepository;

@Path(AppConstants.BASE_PATH + AppConstants.CATALOGUECONFIGURATION_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CatalogueConfigurationRepositoryRs extends RsRepositoryService<CatalogueConfiguration>
{

   private static final long serialVersionUID = 1L;

   public CatalogueConfigurationRepositoryRs()
   {
   }

   @Inject
   public CatalogueConfigurationRepositoryRs(CatalogueConfigurationRepository catalogueConfigurationRepository)
   {
      super(catalogueConfigurationRepository);
      try
      {
         catalogueConfigurationRepository.load();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
   }

}
