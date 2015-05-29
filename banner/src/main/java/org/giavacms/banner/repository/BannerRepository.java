package org.giavacms.banner.repository;

import org.giavacms.api.model.Search;
import org.giavacms.banner.model.Banner;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.BaseRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class BannerRepository extends BaseRepository<Banner>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      // TODO Auto-generated method stub
      return "name asc";
   }

   @Override
   public Banner fetch(Object key)
   {
      try
      {
         Long id;
         if (key instanceof String)
         {
            id = Long.valueOf((String) key);
         }
         else if (key instanceof Long)
         {
            id = (Long) key;
         }
         else
         {
            throw new Exception("key type is not correct!!");
         }
         Banner banner = find(id);
         return banner;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   protected void applyRestrictions(Search<Banner> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      // TYPOLOGY NAME
      if (search.getObj().getBannerType() != null
               && search.getObj().getBannerType().getName() != null
               && search.getObj().getBannerType().getName().trim()
               .length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".bannerType.name = :NAMETYP ");
         params.put("NAMETYP", search.getObj().getBannerType().getName());
      }
      // TYPOLOGY ID
      if (search.getObj().getBannerType() != null
               && search.getObj().getBannerType().getId() != null
               && search.getObj().getBannerType().getId() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".bannerType.id = :IDTYP ");
         params.put("IDTYP", search.getObj().getBannerType().getId());
      }

      // NAME
      if (search.getObj().getName() != null
               && !search.getLike().getName().isEmpty())
      {
         sb.append(separator + " ( upper(").append(alias)
                  .append(".name) LIKE :NAMEPROD ");
         params.put("NAMEPROD", likeParam(search.getLike().getName()
                  .toUpperCase()));
      }

      // DESCRIPTION
      if (search.getObj().getName() != null
               && !search.getLike().getDescription().isEmpty())
      {

         sb.append(separator + "  upper(").append(alias)
                  .append(".description ) LIKE :DESC").append(") ");
         params.put("DESC", likeParam(search.getLike().getDescription()
                  .toUpperCase()));
      }
   }

   public Banner getFirst() throws Exception
   {
      List<Banner> list = getList(new Search<Banner>(Banner.class), 0, 1);
      if (list != null && list.size() > 0)
         return list.get(0);
      return null;
   }

   public List<Banner> getRandomByTypology(String bannerType, int limit) throws Exception
   {
      return getEm()
               .createQuery(
                        "SELECT b FROM Banner b where b.online= :ONLINE AND b.bannerType.name = :TIP ORDER BY RAND()")
               .setParameter("TIP", bannerType).setParameter("ONLINE", true).setMaxResults(limit)
               .getResultList();
   }

   public void updateImage(Long bannerId, Long imageId)
   {
      getEm().createNativeQuery(
               "UPDATE " + Banner.TABLE_NAME + " SET  image_id= :IMAGE_ID WHERE id = :BANNER_ID ")
               .setParameter("BANNER_ID", bannerId).setParameter("IMAGE_ID", imageId)
               .executeUpdate();
   }

   public Image getImage(Long bannerId) throws Exception
   {
      Banner banner = find(bannerId);
      if (banner.getImage() != null && banner.getImage().getId() != null && banner.getImage().getFilename() != null
               && !banner
               .getImage().getFilename()
               .isEmpty())
      {
         return banner.getImage();
      }
      return null;
   }
}
