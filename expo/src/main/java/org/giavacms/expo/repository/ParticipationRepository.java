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
               && search.getObj().getExhibition().getId() != null
               && !search.getObj().getExhibition().getId().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".exhibition.id = :EXHIBITION_ID ");
         params.put("EXHIBITION_ID", search.getObj().getExhibition().getId().trim());
         separator = " and ";
      }
      // DISCIPLINE OBJ
      if (search.getObj() != null && search.getObj().getDiscipline() != null
               && !search.getObj().getDiscipline().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".discipline = :DISCIPLINE ");
         params.put("DISCIPLINE", search.getObj().getDiscipline().trim());
         separator = " and ";
      }

      // ArtifactNAME LIKE
      if (search.getLike() != null && search.getLike().getArtifactname() != null
               && !search.getLike().getArtifactname().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".artifactname ) like :likeArtifactname ");
         params.put("likeArtifactname", likeParam(search.getLike().getArtifactname().trim().toUpperCase()));
         separator = " and ";
      }

      // ARTIFACTNAME LIKE
      if (search.getLike() != null && search.getLike().getArtifactname() != null
               && !search.getLike().getArtifactname().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".artifactname ) like :likeArtifactname ");
         params.put("likeArtifactname", likeParam(search.getLike().getArtifactname().trim().toUpperCase()));
         separator = " and ";
      }

   }

}
