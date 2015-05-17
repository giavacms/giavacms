package org.giavacms.lang.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.lang.model.Language;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
@LocalBean
public class LanguageRepository extends BaseRepository<Language>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "name asc";
   }

}
