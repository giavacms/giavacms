package org.giavacms.banner.service.rs;

import org.giavacms.banner.management.AppConstants;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.banner.model.BannerTypology;
import org.giavacms.banner.repository.BannerTypologyRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.BANNERTYPOLOGY_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BannerTypologyRepositoryRs extends RsRepositoryService<BannerTypology>
{

   private static final long serialVersionUID = 1L;

   @Inject
   public BannerTypologyRepositoryRs(BannerTypologyRepository bannerTypologyRepository)
   {
      super(bannerTypologyRepository);
   }

   public BannerTypologyRepositoryRs()
   {
   }

}
