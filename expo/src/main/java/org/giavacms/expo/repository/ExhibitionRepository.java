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
      if (search.getLike() != null && search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) = :NAME ");
         params.put("NAME", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }

      // ANNO OBJ
      if (search.getObj() != null && search.getObj().getYear() != null && !search.getObj().getYear().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".year = :YEAR ");
         params.put("YEAR", search.getObj().getYear().trim());
         separator = " and ";
      }

      // ID OBJ
      if (search.getObj() != null && search.getObj().getId() != null && !search.getObj().getId().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".id = :ID ");
         params.put("ID", search.getObj().getId().trim());
         separator = " and ";
      }
   }

}
