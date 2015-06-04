package org.giavacms.expo.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Participation;

@Named
@Stateless
@LocalBean
public class ParticipationRepository extends BaseRepository<Participation>
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

      // EXHIBITIONID OBJ
      // DISCIPLINE OBJ
      
      // ARTISTNAME LIKE
      // ARTIFACTNAME LIKE
      
      if (search.getObj() != null && search.getObj().getName() != null)
      {
         sb.append(separator).append(alias).append(".name = :NAME ");
         params.put("NAME", search.getObj().getName());
         separator = " and ";
      }

   }

}
