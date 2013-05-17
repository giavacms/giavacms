package org.giavacms.company.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.company.model.Company;
import org.giavacms.company.repository.CompanyRepository;

@Named
@RequestScoped
public class CompanyRequestController extends
         AbstractRequestController<Company> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(CompanyRepository.class)
   CompanyRepository companyRepository;

   public CompanyRequestController()
   {
      super();
   }

   @Override
   public List<Company> loadPage(int startRow, int pageSize)
   {
      Search<Company> r = new Search<Company>(Company.class);
      r.getObj().setName(getParams().get(SEARCH));
      return companyRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Company> r = new Search<Company>(Company.class);
      r.getObj().setName(getParams().get(SEARCH));
      return companyRepository.getListSize(r);
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

   public Company getPrincipal()
   {
      return companyRepository.findPrincipal();
   }

}
