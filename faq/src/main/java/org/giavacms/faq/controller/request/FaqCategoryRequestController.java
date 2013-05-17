package org.giavacms.faq.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;

@Named
@RequestScoped
public class FaqCategoryRequestController extends
         AbstractRequestController<FaqCategory> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String CATEGORIA = "categoria";
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { CATEGORIA, SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(FaqCategoryRepository.class)
   FaqCategoryRepository faqCategoryRepository;

   public FaqCategoryRequestController()
   {
      super();
   }

   @Override
   public List<FaqCategory> loadPage(int startRow, int pageSize)
   {
      Search<FaqCategory> r = new Search<FaqCategory>(FaqCategory.class);
      return faqCategoryRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<FaqCategory> r = new Search<FaqCategory>(FaqCategory.class);
      return faqCategoryRepository.getListSize(r);
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

}
