package org.giavacms.faq.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;

@Named
@Stateless
@LocalBean
public class FaqRepository extends AbstractPageRepository<Faq>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "faqCategory.orderNum asc,date desc";
   }

   @Override
   protected void applyRestrictions(Search<Faq> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      sb.append(separator).append(alias)
               .append(".faqCategory.active = :activeCategory");
      params.put("activeCategory", true);
      separator = " and ";

      // CATEGORY NAME
      if (search.getObj().getFaqCategory() != null
               && search.getObj().getFaqCategory().getName() != null
               && search.getObj().getFaqCategory().getName().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".faqCategory.name = :NAMECAT ");
         params.put("NAMECAT", search.getObj().getFaqCategory().getName());
      }

      // CATEGORY ID
      if (search.getObj().getFaqCategory() != null
               && search.getObj().getFaqCategory().getId() != null
               && search.getObj().getFaqCategory().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".faqCategory.id = :IDCAT ");
         params.put("IDCAT", search.getObj().getFaqCategory().getId());
      }

      // Answer
      if (search.getObj().getAnswer() != null
               && !search.getObj().getAnswer().isEmpty())
      {
         sb.append(separator + " upper(").append(alias)
                  .append(".answer) LIKE :ANSWER ");
         params.put("ANSWER", likeParam(search.getObj().getAnswer().trim()
                  .toUpperCase()));
      }

      super.applyRestrictions(search, alias, separator, sb, params);
   }

   @Override
   protected Faq prePersist(Faq faq)
   {
      faq.setClone(true);
      faq.setAnswer(HtmlUtils.normalizeHtml(faq.getAnswer()));
      faq.setQuestion(HtmlUtils.normalizeHtml(faq.getQuestion()));
      return super.prePersist(faq);
   }

   @Override
   protected Faq preUpdate(Faq faq)
   {
      faq.setClone(true);
      faq.setAnswer(HtmlUtils.normalizeHtml(faq.getAnswer()));
      faq.setQuestion(HtmlUtils.normalizeHtml(faq.getQuestion()));
      return super.preUpdate(faq);
   }
}
