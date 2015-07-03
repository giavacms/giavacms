package org.giavacms.chalet.repository;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.ChaletParade;

@Stateless
public class ChaletParadeRepository extends BaseRepository<ChaletParade>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "data desc";
   }

   public ChaletParade getLast(String preference) throws Exception
   {
      Search<ChaletParade> sp = new Search<ChaletParade>(ChaletParade.class);
      sp.getObj().setPreference(preference);
      List<ChaletParade> parades = getList(sp, 0, 1);
      return parades == null ? null : parades.isEmpty() ? null : parades.get(0);
   }

   @Override
   protected void applyRestrictions(Search<ChaletParade> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      if (search.getObj().getPreference() != null && !search.getObj().getPreference().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".preference = :preference ");
         params.put("preference", search.getObj().getPreference().trim());
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);
   }
}
