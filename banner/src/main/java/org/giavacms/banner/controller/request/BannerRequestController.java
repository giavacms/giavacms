package org.giavacms.banner.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.banner.model.Banner;
import org.giavacms.banner.repository.BannerRepository;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;

@Named
@RequestScoped
public class BannerRequestController extends AbstractRequestController<Banner>
         implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String TIPOLOGIA = "tipologia";
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { TIPOLOGIA, SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(BannerRepository.class)
   BannerRepository bannerRepository;

   public BannerRequestController()
   {
      super();
   }

   @Override
   protected void init()
   {
      super.init();
   }

   @Override
   public List<Banner> loadPage(int startRow, int pageSize)
   {
      Search<Banner> r = new Search<Banner>(Banner.class);
      r.getObj().setName(getParams().get(SEARCH));
      r.getObj().getBannerTypology().setName(getParams().get(TIPOLOGIA));
      return bannerRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Banner> r = new Search<Banner>(Banner.class);
      r.getObj().getBannerTypology().setName(getParams().get(TIPOLOGIA));
      r.getObj().setName(getParams().get(SEARCH));
      return bannerRepository.getListSize(r);
   }

   @Override
   public String[] getParamNames()
   {
      return PARAM_NAMES;
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
      return bannerRepository.getRandomByTypology(typology);
   }
}
