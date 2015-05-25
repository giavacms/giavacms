package org.giavacms.banner.service.rs;

import org.giavacms.banner.management.AppConstants;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.banner.model.BannerType;
import org.giavacms.banner.repository.BannerTypeRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.BANNERTYPE_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BannerTypeRepositoryRs extends RsRepositoryService<BannerType>
{

   private static final long serialVersionUID = 1L;

   @Inject
   public BannerTypeRepositoryRs(BannerTypeRepository bannerTypeRepository)
   {
      super(bannerTypeRepository);
   }

   public BannerTypeRepositoryRs()
   {
   }

}
