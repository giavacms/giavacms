package org.giavacms.company.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.company.model.Company;
import org.giavacms.company.repository.CompanyRepository;

@Named
@RequestScoped
public class CompanyRequestController extends
         AbstractRequestController<Company> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("q")
   String search;

   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(CompanyRepository.class)
   CompanyRepository companyRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setName(search);
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

   public Company getPrincipal()
   {
      return companyRepository.findPrincipal();
   }

}
