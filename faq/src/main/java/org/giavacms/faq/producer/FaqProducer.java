package org.giavacms.faq.producer;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.common.producer.AbstractProducer;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;

@SessionScoped
@Named
public class FaqProducer extends AbstractProducer implements Serializable
{

   private static final long serialVersionUID = 1L;

   @Inject
   private FaqCategoryRepository faqCategoryRepository;

   @Produces
   @Named
   public SelectItem[] getFaqCategoryItems()
   {
      if (items.get(FaqCategory.class) == null)
      {
         items.put(FaqCategory.class, JSFUtils.setupItems(
                  new Search<FaqCategory>(FaqCategory.class),
                  faqCategoryRepository, "id", "title", "nessuna categoria",
                  "seleziona categoria..."));
      }
      return items.get(FaqCategory.class);
   }

}
