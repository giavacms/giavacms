package org.giavacms.faq.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.faq.management.AppConstants;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.FAQ_CATEGORY_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FaqCategoryRepositoryRs extends RsRepositoryService<FaqCategory>
{

   private static final long serialVersionUID = 1L;

   public FaqCategoryRepositoryRs()
   {
   }

   @Inject
   public FaqCategoryRepositoryRs(FaqCategoryRepository faqCategoryRepository)
   {
      super(faqCategoryRepository);
   }

}
