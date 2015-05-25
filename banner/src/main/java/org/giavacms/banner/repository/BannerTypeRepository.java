package org.giavacms.banner.repository;

import org.giavacms.api.model.Search;
import org.giavacms.banner.model.BannerType;
import org.giavacms.base.repository.BaseRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class BannerTypeRepository extends
         BaseRepository<BannerType>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      // TODO Auto-generated method stub
      return "name asc";
   }

   @Override
   public void delete(Object key) throws Exception
   {

      BannerType bannerType = getEm().find(getEntityType(), key);
      if (bannerType != null)
      {
         bannerType.setActive(false);
         getEm().merge(bannerType);
      }

   }

   @Override
   protected void applyRestrictions(Search<BannerType> search,
            String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";
      if (search.getObj().getName() != null
               && !search.getObj().getName().isEmpty())
      {
         sb.append(separator).append(" upper(").append(alias)
                  .append(".name ) like :NAME ");
         params.put("NAME", likeParam(search.getObj().getName()
                  .toUpperCase()));
      }
   }

}
