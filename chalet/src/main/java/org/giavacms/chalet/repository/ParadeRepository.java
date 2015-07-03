package org.giavacms.chalet.repository;

import java.util.Map;

import javax.ejb.Stateless;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.Parade;

@Stateless
public class ParadeRepository extends BaseRepository<Parade>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "data desc";
   }

   @Override
   protected void applyRestrictions(Search<Parade> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      if (search.getObj().getName() != null && !search.getObj().getName().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".name = :name ");
         params.put("name", search.getObj().getName().trim());
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);
   }
}
