package org.giavacms.security.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.security.management.AppConstants;
import org.giavacms.security.model.EmailConfiguration;
import org.giavacms.security.repository.EmailConfigurationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.EMAILCONFIGURATION_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmailConfigurationRepositoryRs extends RsRepositoryService<EmailConfiguration>
{

   private static final long serialVersionUID = 1L;

   public EmailConfigurationRepositoryRs()
   {
   }

   @Inject
   public EmailConfigurationRepositoryRs(EmailConfigurationRepository emailConfigurationRepository)
   {
      super(emailConfigurationRepository);
      try
      {
         emailConfigurationRepository.load();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
   }

}
