package org.giavacms.exhibition.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.exhibition.model.Exhibition;
import org.giavacms.exhibition.repository.ExhibitionRepository;

@Named
@RequestScoped
public class ExhibitionRequestController extends
         AbstractRequestController<Exhibition> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String EXHIBITION = "exhibition";
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { EXHIBITION,
            SEARCH };
   public static final String ID_PARAM = "exhibition";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(ExhibitionRepository.class)
   ExhibitionRepository exhibitionRepository;

   public ExhibitionRequestController()
   {
      super();
   }

   @Override
   protected void init()
   {
      super.init();
   }

   @Override
   public List<Exhibition> loadPage(int startRow, int pageSize)
   {
      Search<Exhibition> r = new Search<Exhibition>(Exhibition.class);
      r.getObj().setName(getParams().get(SEARCH));
      return exhibitionRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Exhibition> r = new Search<Exhibition>(Exhibition.class);
      r.getObj().setName(getParams().get(SEARCH));
      return exhibitionRepository.getListSize(r);
   }

   public List<Exhibition> getAll()
   {
      return exhibitionRepository.getAll();
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

   public String viewElement(String id)
   {
      setElement(exhibitionRepository.fetch(id));
      return viewPage();
   }

}
