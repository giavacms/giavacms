package org.giavacms.expo.repository;

import org.giavacms.api.model.Search;
import org.giavacms.api.util.IdUtils;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Participation;
import org.giavacms.expo.model.pojo.Discipline;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
      // ARTISTID OBJ
      if (search.getObj() != null && search.getObj().getArtist() != null
               && search.getObj().getArtist().getId() != null
               && !search.getObj().getArtist().getId().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".artist.id = :ARTIST_ID ");
         params.put("ARTIST_ID", search.getObj().getArtist().getId().trim());
         separator = " and ";
      }

      // ARTISTNAME LIKE
      if (search.getLike() != null && search.getLike().getArtist() != null
               && search.getLike().getArtist().getName() != null
               && !search.getLike().getArtist().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".artist.name ) like :ARTIST_NAME ");
         params.put("ARTIST_NAME", likeParam(search.getLike().getArtist().getName().trim().toUpperCase()));
         separator = " and ";

         sb.append(separator).append(alias).append(".artist.name = :ARTIST_NAME ");
         params.put("ARTIST_NAME", search.getObj().getArtist().getName().trim());
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

      // PARTICIPATIONTYPE OBJ
      if (search.getObj() != null && search.getObj().getParticipationtype() != null
               && !search.getObj().getParticipationtype().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".participationType = :participationType ");
         params.put("participationType", search.getObj().getParticipationtype().trim());
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

   public List<Discipline> getDisciplinesByExhibitionId(String exhibitionId)
   {
      @SuppressWarnings("unchecked")
      List<Object[]> results = getEm()
               .createNativeQuery(
                        "select count(*),discipline from " + Participation.TABLE_NAME
                                 + " where exhibition_id = :exhibitionId group by discipline ")
               .setParameter("exhibitionId", exhibitionId).getResultList();
      List<Discipline> disciplines = new ArrayList<Discipline>();
      for (Object[] row : results)
      {
         Discipline discipline = new Discipline();
         discipline.setParticipants(Integer.parseInt(row[0].toString()));
         discipline.setName((String) row[1]);
         discipline.setExhibitionId(exhibitionId);
         disciplines.add(discipline);
      }
      Collections.sort(disciplines);
      return disciplines;
   }

   @Override
   protected Participation prePersist(Participation p)
   {
      String id = IdUtils.createPageId(p.getArtifactname());
      String idFinal = makeUniqueKey(id, Participation.TABLE_NAME);
      p.setId(idFinal);
      return p;
   }

   @Override
   protected Participation preUpdate(Participation n)
   {
      return n;
   }
}
