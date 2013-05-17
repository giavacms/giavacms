package org.giavacms.faq.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.repository.FaqRepository;

@Named
@RequestScoped
public class FaqRequestController extends AbstractRequestController<Faq>
         implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String CATEGORIA = "categoria";
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { CATEGORIA, SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(FaqRepository.class)
   FaqRepository faqRepository;

   public FaqRequestController()
   {
      super();
   }

   @Override
   public List<Faq> loadPage(int startRow, int pageSize)
   {
      System.out.println("load page");
      Search<Faq> r = new Search<Faq>(Faq.class);
      r.getObj().setQuestion(getParams().get(SEARCH));
      r.getObj().getFaqCategory().setName(getParams().get(CATEGORIA));
      return faqRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      System.out.println("load page size");
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Faq> r = new Search<Faq>(Faq.class);
      r.getObj().setQuestion(getParams().get(SEARCH));
      r.getObj().getFaqCategory().setName(getParams().get(CATEGORIA));
      return faqRepository.getListSize(r);
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
