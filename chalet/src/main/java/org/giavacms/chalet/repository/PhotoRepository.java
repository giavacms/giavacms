package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.Photo;

import javax.ejb.Stateless;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.HashMap;
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

      if (true)
      {
         sb.append(separator).append(alias).append(".active = :attivoTrue ");
         params.put("attivoTrue", true);
         separator = " and ";
      }
      
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
         params.put("approvedFalse", false);
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

   public List<Chalet> withPhoto(Search<Photo> search) throws Exception
   {
      Map<String, Object> params = new HashMap<String, Object>();
      String alias = " p ";
      StringBuffer sb = new StringBuffer("select ").append(alias).append(".chaletId, ").append(alias)
               .append(".chaletName from ").append(Photo.class.getSimpleName()).append(
                        alias);
      String separator = " where ";
      applyRestrictions(search, alias, separator, sb, params);
      sb.append(" group by ").append(alias).append(".chaletId, ").append(alias).append(".chaletName");
      Query q = getEm().createQuery(sb.toString());
      for (String param : params.keySet())
      {
         q.setParameter(param, params.get(param));
      }
      @SuppressWarnings("unchecked")
      List<Object[]> results = (List<Object[]>) q.getResultList();

      List<Chalet> chalets = new ArrayList<Chalet>();
      for (Object[] result : results)
      {
         Chalet chalet = new Chalet();
         chalet.setActive(true);
         chalet.setId((String) result[0]);
         chalet.setName((String) result[1]);
         chalets.add(chalet);
      }
      return chalets;
   }
}