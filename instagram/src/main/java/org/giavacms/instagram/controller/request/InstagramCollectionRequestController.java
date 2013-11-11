package org.giavacms.instagram.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.instagram.model.InstagramCollection;
import org.giavacms.instagram.repository.InstagramCollectionRepository;
import org.giavacms.instagram.repository.InstagramPhotoRepository;

@Named
@RequestScoped
public class InstagramCollectionRequestController extends
         AbstractRequestController<InstagramCollection> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam(ID_PARAM)
   String id;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String start;

   @Inject
   @HttpParam("q")
   String search;

   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(InstagramCollectionRepository.class)
   InstagramCollectionRepository instagramCollectionRepository;

   @Inject
   InstagramPhotoRepository instagramPhotoRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setName(search);
      super.initSearch();
   }

   public List<InstagramCollection> getAllCollections()
   {
      Search<InstagramCollection> r = new Search<InstagramCollection>(
               InstagramCollection.class);
      List<InstagramCollection> list = instagramCollectionRepository.getList(
               r, 0, 0);
      return list;
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

}
