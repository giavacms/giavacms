package org.giavacms.insuranceclaim.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.insuranceclaim.model.InsuranceClaimProduct;
import org.giavacms.insuranceclaim.repository.InsuranceClaimCategoryRepository;
import org.giavacms.insuranceclaim.repository.InsuranceClaimProductRepository;

@Named
@RequestScoped
public class InsuranceClaimProductRequestController extends
         AbstractRequestController<InsuranceClaimProduct> implements
         Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("categoria")
   String category;
   @Inject
   @HttpParam("q")
   String search;

   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";
   @HttpParam(ID_PARAM)
   @Inject
   String id;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String start;

   @Inject
   @OwnRepository(InsuranceClaimProductRepository.class)
   InsuranceClaimProductRepository insuranceClaimProductRepository;

   @Inject
   InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setName(search);
      getSearch().getObj().getInsuranceClaimCategory()
               .setName(category);
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

   public InsuranceClaimProduct getFirst()
   {
      return insuranceClaimProductRepository.getFirst();
   }

}
