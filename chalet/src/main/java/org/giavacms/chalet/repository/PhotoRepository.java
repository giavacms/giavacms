package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.Photo;

import javax.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class PhotoRepository extends BaseRepository<Photo>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return " created desc";
   }

   @Override
   protected void applyRestrictions(Search<Photo> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // le approvate
      if (search.getObj().isApproved())
      {
         sb.append(separator).append(alias).append(".approved = :approvedTrue ");
         params.put("approvedTrue", true);
         separator = " and ";
         // inutile.. sarà sempre valorizzata se approved true
         // sb.append(separator).append(alias).append(".approvedDate is not null ");
         // separator = " and ";
      }

      // le non approvate
      if (search.getNot().isApproved())
      {
         sb.append(separator).append(alias).append(".approved = :approvedFalse ");
         params.put("approvedFalse", true);
         separator = " and ";
         sb.append(separator).append(alias).append(".approvedDate is not null ");
         separator = " and ";
      }

      // le valutate in genere
      if (search.getObj().getApprovedDate() != null)
      {
         sb.append(separator).append(alias).append(".approvedDate is not null ");
         separator = " and ";
      }

      // le sospese
      if (search.getNot().getApprovedDate() != null)
      {
         sb.append(separator).append(alias).append(".approvedDate is null ");
         separator = " and ";
      }

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

   public List<Chalet> withPhoto(String chaletId, String accountId, Boolean approved, Boolean evaluated)
   {
      List<Object[]> results = getEm().createNativeQuery("select distinct ...").getResultList();
      return new ArrayList<Chalet>();
   }

}