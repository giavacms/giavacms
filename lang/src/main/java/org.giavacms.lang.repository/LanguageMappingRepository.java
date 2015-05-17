package org.giavacms.lang.repository;

import org.giavacms.lang.model.LanguageMapping;
import org.giavacms.base.repository.BaseRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
@LocalBean
public class LanguageMappingRepository extends BaseRepository<LanguageMapping>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "pageId asc";
   }

}
