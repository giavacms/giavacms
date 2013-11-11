package org.giavacms.banner.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.banner.model.Banner;
import org.giavacms.banner.repository.BannerRepository;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;

@Named
@RequestScoped
public class BannerRequestController extends AbstractRequestController<Banner>
         implements Serializable
{

   private static final long serialVersionUID = 1L;

   private static final String ID_PARAM = "id";
   private static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @HttpParam("tipologia")
   String tipology;

   @Inject
   @HttpParam("q")
   String name;

   @Inject
   @HttpParam(ID_PARAM)
   String idParam;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String currentPageParam;

   @Inject
   @OwnRepository(BannerRepository.class)
   BannerRepository bannerRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setName(name);
      getSearch().getObj().getBannerTypology().setName(tipology);
      super.initSearch();
   }

   @Override
   public String getIdParam()
   {
      return ID_PARAM;
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   public boolean isScheda()
   {
      return getElement() != null && getElement().getId() != null;
   }

   public Banner getRandomByTypology(String typology)
   {
      logger.info("typology: " + typology);
      return bannerRepository.getRandomByTypology(typology, 0).get(0);
   }

   public List<Banner> getRandomByTypologyAndLimit(String typology, int limit)
   {
      logger.info("typology: " + typology);
      return bannerRepository.getRandomByTypology(typology, limit);
   }
}
