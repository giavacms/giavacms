package org.giavacms.expo.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Participation;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

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
   protected void applyRestrictions(Search<Participation> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // EXHIBITIONID OBJ
      if (search.getObj() != null && search.getObj().getExhibition() != null
               && search.getObj().getExhibition().getId() != null)
      {
         sb.append(separator).append(alias).append(".exhibition.id = :EXHIBITION_ID ");
         params.put("EXHIBITION_ID", search.getObj().getExhibition().getId());
         separator = " and ";
      }
      // DISCIPLINE OBJ
      if (search.getObj() != null && search.getObj().getDiscipline() != null)
      {
         sb.append(separator).append(alias).append(".discipline = :DISCIPLINE ");
         params.put("DISCIPLINE", search.getObj().getDiscipline());
         separator = " and ";
      }

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
