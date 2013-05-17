package org.giavacms.insuranceclaim.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

   public static final String CATEGORIA = "categoria";
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { CATEGORIA, SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(InsuranceClaimProductRepository.class)
   InsuranceClaimProductRepository insuranceClaimProductRepository;

   @Inject
   InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

   public InsuranceClaimProductRequestController()
   {
      super();
   }

   @Override
   public List<InsuranceClaimProduct> loadPage(int startRow, int pageSize)
   {
      Search<InsuranceClaimProduct> r = new Search<InsuranceClaimProduct>(
               InsuranceClaimProduct.class);
      r.getObj().setName(getParams().get(SEARCH));
      r.getObj().getInsuranceClaimCategory()
               .setName(getParams().get(CATEGORIA));
      return insuranceClaimProductRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<InsuranceClaimProduct> r = new Search<InsuranceClaimProduct>(
               InsuranceClaimProduct.class);
      r.getObj().getInsuranceClaimCategory()
               .setName(getParams().get(CATEGORIA));
      r.getObj().setName(getParams().get(SEARCH));
      return insuranceClaimProductRepository.getListSize(r);
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

   public InsuranceClaimProduct getFirst()
   {
      return insuranceClaimProductRepository.getFirst();
   }

}
