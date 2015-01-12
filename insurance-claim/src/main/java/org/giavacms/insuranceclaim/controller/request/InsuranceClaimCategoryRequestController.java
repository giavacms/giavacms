package org.giavacms.insuranceclaim.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.insuranceclaim.model.InsuranceClaimCategory;
import org.giavacms.insuranceclaim.repository.InsuranceClaimCategoryRepository;

@Named
@RequestScoped
public class InsuranceClaimCategoryRequestController extends
         AbstractRequestController<InsuranceClaimCategory> implements
         Serializable
{

   private static final long serialVersionUID = 1L;

   @HttpParam("tipologia")
   @Inject
   String typology;

   @HttpParam("categoria")
   @Inject
   String category;

   @HttpParam("q")
   @Inject
   String search;
   @HttpParam(ID_PARAM)
   @Inject
   String id;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String start;
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(InsuranceClaimCategoryRepository.class)
   InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

   public List<InsuranceClaimCategory> getCategorie(String tipologia)
   {
      Search<InsuranceClaimCategory> r = new Search<InsuranceClaimCategory>(
               InsuranceClaimCategory.class);
      r.getObj().getInsuranceClaimTypology().setName(tipologia);
      return insuranceClaimCategoryRepository.getList(r, 0, 0);
   }

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setName(search);
      getSearch().getObj().getInsuranceClaimTypology().setName(typology);
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

   public InsuranceClaimCategory getElementByName()
   {
      if (category != null
               && !category.isEmpty())
      {
         logger.info("CATEGORIA: " + category);
         return insuranceClaimCategoryRepository.findByName(category);
      }
      return new InsuranceClaimCategory();
   }

   public List<InsuranceClaimCategory> getAllCategories(String tipologia)
   {
      return insuranceClaimCategoryRepository.fetchAll(tipologia);
   }
}
