package org.giavacms.faq.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;

@Named
@RequestScoped
public class FaqCategoryRequestController extends
         AbstractRequestController<FaqCategory> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("t")
   String category;

   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @HttpParam(ID_PARAM)
   String id;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String start;

   @Inject
   @OwnRepository(FaqCategoryRepository.class)
   FaqCategoryRepository faqCategoryRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setTitle(category);
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

}
