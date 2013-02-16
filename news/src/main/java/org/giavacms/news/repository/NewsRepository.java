package org.giavacms.news.repository;

import java.util.Date;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.controller.util.TimeUtils;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.news.model.News;
import org.giavacms.news.model.type.NewsType;


@Named
@Stateless
@LocalBean
public class NewsRepository extends AbstractRepository<News>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   public EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected void applyRestrictions(Search<News> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      if (search.getObj().getNewsType() != null
               && search.getObj().getNewsType().getName() != null
               && search.getObj().getNewsType().getName().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".newsType.name = :newsTypeName ");
         params.put("newsTypeName", search.getObj().getNewsType().getName());
      }
      if (search.getObj().getNewsType() != null
               && search.getObj().getNewsType().getId() != null)
      {
         sb.append(separator).append(alias)
                  .append(".newsType.id = :idNewsType ");
         params.put("idNewsType", search.getObj().getNewsType().getId());
      }

      if (search.getObj().getTitle() != null
               && !search.getObj().getTitle().isEmpty())
      {
         sb.append(separator + " (").append(alias)
                  .append(".title LIKE :title ");
         params.put("titolo", likeParam(search.getObj().getTitle()));
         sb.append(" or ").append(alias).append(".content LIKE :content ");
         params.put("content", likeParam(search.getObj().getTitle()));
         sb.append(" ) ");
      }

   }

   @Override
   protected News prePersist(News n)
   {
      String idTitle = PageUtils.createPageId(n.getTitle());
      String idFinal = testKey(idTitle);
      n.setId(idFinal);
      if (n.getDate() == null)
         n.setDate(new Date());
      if (n.getNewsType() != null && n.getNewsType().getId() != null)
         n.setNewsType(getEm().find(NewsType.class, n.getNewsType().getId()));
      n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
      return n;
   }

   @Override
   protected News preUpdate(News n)
   {
      if (n.getDate() == null)
         n.setDate(new Date());
      if (n.getNewsType() != null && n.getNewsType().getId() != null)
         n.setNewsType(getEm().find(NewsType.class, n.getNewsType().getId()));
      n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
      return n;
   }

   public News findLast()
   {
      News ret = new News();
      try
      {
         ret = (News) em
                  .createQuery("select p from Notizia p order by p.id desc ")
                  .setMaxResults(1).getSingleResult();

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return ret;
   }

   @Override
   public News fetch(Object key)
   {
      return super.fetch(key);
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "data desc";
   }
}
