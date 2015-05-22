package org.giavacms.faq.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.faq.model.FaqCategory;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
@LocalBean
public class FaqCategoryRepository extends BaseRepository<FaqCategory>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "orderNum asc";
   }

   @Override
   protected FaqCategory prePersist(FaqCategory n) throws Exception
   {
      return super.prePersist(n);
   }

   @Override
   protected FaqCategory preUpdate(FaqCategory n) throws Exception
   {
      return super.preUpdate(n);
   }

}
