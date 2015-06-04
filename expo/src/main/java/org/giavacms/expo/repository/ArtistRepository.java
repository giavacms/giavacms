package org.giavacms.expo.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.expo.model.Artist;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

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
}
