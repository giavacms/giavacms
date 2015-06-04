package org.giavacms.expo.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Partecipation;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class PartecipationRepository extends BaseRepository<Partecipation>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "id desc";
   }

   @Override
   protected void applyRestrictions(Search<Partecipation> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // NAME
      if (search.getObj() != null && search.getObj().getName() != null)
      {
         sb.append(separator).append(alias).append(".name = :NAME ");
         params.put("NAME", search.getObj().getName());
         separator = " and ";
      }

   }

}
