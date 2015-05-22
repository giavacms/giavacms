package org.giavacms.faq.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.faq.model.Faq;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
@LocalBean
public class FaqRepository extends BaseRepository<Faq>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "faqCategory.orderNum asc,date desc";
   }

}
