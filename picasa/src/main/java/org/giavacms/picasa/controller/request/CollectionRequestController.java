package org.giavacms.picasa.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.picasa.model.Collection;
import org.giavacms.picasa.repository.CollectionRepository;
import org.giavacms.picasa.repository.PhotoRepository;

@Named
@RequestScoped
public class CollectionRequestController extends
         AbstractRequestController<Collection> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(CollectionRepository.class)
   CollectionRepository collectionRepository;

   @Inject
   PhotoRepository photoRepository;

   public CollectionRequestController()
   {
      super();
   }

   @Override
   public List<Collection> loadPage(int startRow, int pageSize)
   {
      Search<Collection> r = new Search<Collection>(Collection.class);
      r.getObj().setName(getParams().get(SEARCH));
      return collectionRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Collection> r = new Search<Collection>(Collection.class);
      r.getObj().setName(getParams().get(SEARCH));
      return collectionRepository.getListSize(r);
   }

   public List<Collection> getAllCollections()
   {
      Search<Collection> r = new Search<Collection>(Collection.class);
      List<Collection> list = collectionRepository.getList(r, 0, 0);
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
