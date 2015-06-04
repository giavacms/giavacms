package org.giavacms.expo.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Exhibition;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class ExhibitionRepository extends BaseRepository<Exhibition>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "id desc";
   }

   @Override
   protected void applyRestrictions(Search<Exhibition> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // NAME LIKE

      if (search.getLike() != null && search.getLike().getName() != null)
      {
         sb.append(separator).append(alias).append(".name = :NAME ");
         params.put("NAME", search.getLike().getName());
         separator = " and ";
      }
      // ANNO OBJ
      if (search.getObj() != null && search.getObj().getYear() != null)
      {
         sb.append(separator).append(alias).append(".year = :YEAR ");
         params.put("YEAR", search.getObj().getYear());
         separator = " and ";
      }
      // ID OBJ
      if (search.getObj() != null && search.getObj().getId() != null)
      {
         sb.append(separator).append(alias).append(".id = :ID ");
         params.put("ID", search.getObj().getId());
         separator = " and ";
      }
   }

}
