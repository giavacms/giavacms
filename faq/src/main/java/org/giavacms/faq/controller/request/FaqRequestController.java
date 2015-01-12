package org.giavacms.faq.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageRequestController;
import org.giavacms.base.pojo.I18nRequestParams;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;
import org.giavacms.faq.repository.FaqRepository;

@Named
@RequestScoped
public class FaqRequestController
         extends AbstractPageRequestController<Faq> implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "f_p";

   public static final String CATEGORY_PARAM = "f_c";
   public static final String SEARCH_PARAM = "f_q";
   @Inject
   @HttpParam(CATEGORY_PARAM)
   String category;
   @Inject
   @HttpParam(SEARCH_PARAM)
   String search;
   @Inject
   @HttpParam(ID_PARAM)
   String id;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String start;
   @Inject
   @OwnRepository(FaqRepository.class)
   FaqRepository faqRepository;

   @Inject
   FaqCategoryRepository faqCategoryRepository;

   public List<FaqCategory> getFaqCategories()
   {
      Search<FaqCategory> r = new Search<FaqCategory>(FaqCategory.class);
      // elementi della stessa lingua della pagina base
      r.getObj().setLang(super.getBasePage().getLang());
      return faqCategoryRepository.getList(r, 0, 0);
   }

   @Override
   protected void initSearch()
   {
      super.getSearch().getObj().setTitle(search);
      super.getSearch().getObj().getFaqCategory().setId(category);
      super.initSearch();
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

   @Override
   protected void handleI18N()
   {

      I18nRequestParams i18nRequestParams = super.getI18nRequestParams();

      int currentLang = getBasePage().getLang();

      String currentLangValue = i18nRequestParams.get(currentLang, CATEGORY_PARAM);
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
                              CATEGORY_PARAM,
                              (i18nRequestParams.getLanguages()[i] == null || !i18nRequestParams
                                       .getLanguages()[i].isEnabled()) ? "n.a."
                                       : faqCategoryRepository.translate(currentLangValue, currentLang, i,
                                                currentLangValue));
         }
      }
   }

}
