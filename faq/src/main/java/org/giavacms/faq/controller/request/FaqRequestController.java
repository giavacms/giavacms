package org.giavacms.faq.controller.request;

import java.io.Serializable;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageRequestController;
import org.giavacms.base.pojo.I18nRequestParams;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.repository.FaqCategoryRepository;
import org.giavacms.faq.repository.FaqRepository;

@Named
@RequestScoped
public class FaqRequestController
         extends AbstractPageRequestController<Faq> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String CATEGORIA = "f_c";
   public static final String SEARCH = "f_q";
   public static final String[] PARAM_NAMES = new String[] { CATEGORIA, SEARCH };
   public static final String CURRENT_PAGE_PARAM = "f_p";

   @Inject
   @OwnRepository(FaqRepository.class)
   FaqRepository faqRepository;

   @Inject
   FaqCategoryRepository faqCategoryRepository;

   public FaqRequestController()
   {
      super();
   }

   @Override
   public void initParameters()
   {
      super.initParameters();
      this.handleI18N();
   }

   @Override
   public List<Faq> loadPage(int startRow, int pageSize)
   {
      System.out.println("load page");
      return faqRepository.getList(buildSearch(), startRow, pageSize);
   }

   private Search<Faq> buildSearch()
   {
      Search<Faq> r = new Search<Faq>(Faq.class);
      r.getObj().setTitle(getParams().get(SEARCH));
      r.getObj().getFaqCategory().setId(getParams().get(CATEGORIA));
      // elementi della stessa lingua della pagina base
      r.getObj().setLang(super.getBasePage().getLang());
      return r;
   }

   @Override
   public int totalSize()
   {
      System.out.println("load page size");
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      return faqRepository.getListSize(buildSearch());
   }

   @Override
   public String[] getParamNames()
   {
      return PARAM_NAMES;
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   /**
    * What does it mean?
    * 
    * @return
    */
   public boolean isScheda()
   {
      return getElement() != null && getElement().getId() != null;
   }

   protected void handleI18N()
   {

      I18nRequestParams i18nRequestParams = super.getI18nRequestParams();

      int currentLang = getBasePage().getLang();

      String currentLangValue = i18nRequestParams.get(currentLang, CATEGORIA);
      if (currentLangValue != null && currentLangValue.trim().length() > 0)
      {
         for (int i = 0; i < i18nRequestParams.getLanguages().length; i++)
         {
            if (i == currentLang)
            {
               continue;
            }
            i18nRequestParams
                     .put(i,
                              CATEGORIA,
                              (i18nRequestParams.getLanguages()[i] == null || !i18nRequestParams
                                       .getLanguages()[i].isEnabled()) ? "n.a."
                                       : faqCategoryRepository.translate(currentLangValue, currentLang, i, currentLangValue));
         }
      }
   }

}
