package org.giavacms.expo.repository;

import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.api.model.Search;
import org.giavacms.api.util.IdUtils;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Artist;
import org.giavacms.expo.model.Exhibition;
import org.giavacms.expo.model.Participation;

@Named
@Stateless
@LocalBean
public class ArtistRepository extends BaseRepository<Artist>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "id desc";
   }

   @Override
   protected void applyRestrictions(Search<Artist> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      // NAME LIKE
      if (search.getLike() != null && search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) like :likeName ");
         params.put("likeName", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }

      // SURNAME LIKE
      if (search.getLike() != null && search.getLike().getSurname() != null
               && !search.getLike().getSurname().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".surname ) like :likeSurname ");
         params.put("likeSurname", likeParam(search.getLike().getSurname().trim().toUpperCase()));
         separator = " and ";
      }

      // STAGENAME LIKE
      if (search.getLike() != null && search.getLike().getStagename() != null
               && !search.getLike().getStagename().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".stagename ) like :likeStagename ");
         params.put("likeStagename", likeParam(search.getLike().getStagename().trim().toUpperCase()));
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public void importYear(String year) throws Exception
   {
      int bimCounter = 0;
      Exhibition exhibition = getEm().find(Exhibition.class, "arte-insieme-" + year);
      @SuppressWarnings("unchecked")
      List<Object[]> results = getEm()
               .createNativeQuery(
                        "SELECT "
                                 // ..0..1......2........3..........4.........5......6..........7.........8......9..........10.............11..................12..........13...........14.........15......16.............17......18.........19.......20.........21
                                 + " id, nome, cognome, nomedarte, telefono, email, biografia, nomeopera, data, dimensioni, materiale, descrizione_sintetica, disciplina, revisionata, consegnata, note, partecipazione, sitoWeb, facebook, twitter, instagram, catalogo "
                                 +
                                 " FROM arte_insieme" + year).getResultList();
      for (Object[] row : results)
      {
         String nome = (String) row[1];
         String cognome = (String) row[2];
         String artistName = (nome == null ? "" : (nome + " ")) + (cognome == null ? "" : cognome);
         String artistId = IdUtils.createPageId(artistName);
         // * @return the found entity instance or null if the entity does
         // * not exist
         Artist artist = find(artistId);
         if (artist == null)
         {
            artist = new Artist();
            artist.setBiography((String) row[6]);
            artist.setEmail((String) row[5]);
            artist.setFacebook((String) row[18]);
            artist.setId(artistId);
            artist.setInstagram((String) row[20]);
            artist.setName((String) row[1]);
            artist.setStagename((String) row[3]);
            artist.setSurname((String) row[2]);
            artist.setTelephone((String) row[4]);
            artist.setTwitter((String) row[19]);
            artist.setWebsite((String) row[17]);
            persist(artist);
         }
         Participation p = new Participation();
         p.setArtist(artist);
         p.setArtifactname((String) row[7]);
         p.setArtistname(artistName);
         p.setCatalogues((Integer) row[21]);
         p.setContest("bim".equals(row[16]) ? true : false);
         p.setCreationdate((String) row[8]);
         p.setDelivered(((Integer) row[14]) > 0 ? true : false);
         p.setDimensions((String) row[9]);
         p.setDiscipline((String) row[12]);
         p.setExhibition(exhibition);
         p.setMaterial((String) row[10]);
         p.setNote((String) row[15]);
         p.setParticipationtype((String) row[16]);
         p.setPreview((String) row[11]);
         p.setReviewed(((Integer) row[13]) > 0 ? true : false);
         if (p.isContest())
         {
            p.setCustom(++bimCounter + "");
         }
         getEm().persist(p);
      }
   }
}
