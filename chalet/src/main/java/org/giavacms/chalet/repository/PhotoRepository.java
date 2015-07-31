package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.Photo;

import javax.ejb.Stateless;
import java.util.Map;

@Stateless
public class PhotoRepository extends BaseRepository<Photo>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "created desc";
   }

   @Override
   protected void applyRestrictions(Search<Photo> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // NAME
      if (search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) like :likeName ");
         params.put("likeName", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }

      // CHALET ID
      if (search.getObj().getChaletId() != null
               && !search.getObj().getChaletId().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".chaletId = :chaletId ");
         params.put("chaletId", search.getObj().getChaletId());
         separator = " and ";
      }

      // ACCOUNT ID
      if (search.getObj().getAccountId() != null
               && !search.getObj().getAccountId().trim().isEmpty())
      {
         sb.append(separator).append(alias).append(".accountId = :accountId ");
         params.put("accountId", search.getObj().getAccountId());
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   @Override
   public void delete(Object key) throws Exception
   {
      getEm().createNativeQuery("update " + Photo.TABLE_NAME + " set active = :active where uuid = :uuid")
               .setParameter("active", false).setParameter("uuid", key).executeUpdate();
   }

}
