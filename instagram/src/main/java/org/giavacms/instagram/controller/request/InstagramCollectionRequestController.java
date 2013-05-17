package org.giavacms.instagram.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(InstagramCollectionRepository.class)
   InstagramCollectionRepository instagramCollectionRepository;

   @Inject
   InstagramPhotoRepository instagramPhotoRepository;

   public InstagramCollectionRequestController()
   {
      super();
   }

   @Override
   public List<InstagramCollection> loadPage(int startRow, int pageSize)
   {
      Search<InstagramCollection> r = new Search<InstagramCollection>(
               InstagramCollection.class);
      r.getObj().setName(getParams().get(SEARCH));
      return instagramCollectionRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<InstagramCollection> r = new Search<InstagramCollection>(
               InstagramCollection.class);
      r.getObj().setName(getParams().get(SEARCH));
      return instagramCollectionRepository.getListSize(r);
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

}
