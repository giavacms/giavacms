package org.giavacms.lang.service.rs;

import org.giavacms.api.management.AppConstants;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.lang.model.LanguageMapping;
import org.giavacms.lang.repository.LanguageMappingRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.LANGUAGE_MAPPING_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LanguageMappingRepositoryRs extends RsRepositoryService<LanguageMapping>
{

   private static final long serialVersionUID = 1L;

   @Inject
   LanguageMappingRepository languageMappingRepository;

   public LanguageMappingRepositoryRs()
   {
   }

   @Inject
   public LanguageMappingRepositoryRs(LanguageMappingRepository languageMappingRepository)
   {
      super(languageMappingRepository);
   }

   @Override protected void postPersist(LanguageMapping languageMapping) throws Exception
   {
      //WE MUST CREATE THE MUTUALS
      LanguageMapping languageMapping1;
      LanguageMapping languageMapping2;
      LanguageMapping languageMapping3;
      LanguageMapping languageMapping4;
      LanguageMapping languageMapping5;
      if (!languageMapping.getPageId1().isEmpty())
      {
         languageMapping1 = new LanguageMapping();
      }
      if (!languageMapping.getPageId2().isEmpty())
      {
         languageMapping2 = new LanguageMapping();
      }
      if (!languageMapping.getPageId3().isEmpty())
      {
         languageMapping3 = new LanguageMapping();
      }
      if (!languageMapping.getPageId4().isEmpty())
      {
         languageMapping4 = new LanguageMapping();
      }
      if (!languageMapping.getPageId5().isEmpty())
      {
         languageMapping5 = new LanguageMapping();
      }
      //      super.postPersist(object);
   }
}
