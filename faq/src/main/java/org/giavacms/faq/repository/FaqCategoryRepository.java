package org.giavacms.faq.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.FaqCategory;

@Named
@Stateless
@LocalBean
public class FaqCategoryRepository extends AbstractPageRepository<FaqCategory>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "orderNum asc";
   }

   @Override
   public FaqCategory fetch(Object key)
   {
      FaqCategory faqCategory = super.fetch(key);
      if (faqCategory != null)
      {
         if (faqCategory.getImage() != null)
         {
            faqCategory.getImage().toString();
            if (faqCategory.getImage().getData() != null)
            {
               faqCategory.getImage().getData().toString();
            }
         }
      }
      return faqCategory;
   }

   @Override
   protected void applyRestrictions(Search<FaqCategory> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      // NEVER FORGET THIS IF YOU NEED TO OVERRIDE THIS METHOD
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   @Override
   protected FaqCategory prePersist(FaqCategory n)
   {
      n.setClone(true);
      return super.prePersist(n);
   }

   @Override
   protected FaqCategory preUpdate(FaqCategory n)
   {
      n.setClone(true);
      return super.preUpdate(n);
   }

}
