package org.giavacms.faq.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;
import org.giavacms.faq.repository.FaqRepository;

@Named
@RequestScoped
public class GroupedFaqRequestController implements Serializable
{

   private static final long serialVersionUID = 1L;

   @Inject
   FaqCategoryRepository faqCategoryRepository;

   @Inject
   FaqRepository faqRepository;

   List<FaqCategory> getFaqCategories()
   {
      return faqCategoryRepository.getList(new Search<FaqCategory>(FaqCategory.class), 0, 0);
   }

   List<Faq> getFaqsByFaqCategoryId(String faqCategoryId)

   {
      Search<Faq> sf = new Search<Faq>(Faq.class);
      sf.getObj().setFaqCategory(new FaqCategory());
      sf.getObj().getFaqCategory().setId(faqCategoryId);
      return faqRepository.getList(sf, 0, 0);
   }
}
