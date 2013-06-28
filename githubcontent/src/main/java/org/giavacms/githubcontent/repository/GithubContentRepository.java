package org.giavacms.githubcontent.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.githubcontent.util.GithubImporter;
import org.giavacms.richcontent.model.RichContent;

@Named
@Stateless
@LocalBean
public class GithubContentRepository extends AbstractPageRepository<RichContent>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "date desc";
   }

   @Override
   protected RichContent prePersist(RichContent n)
   {
      throw new RuntimeException("Trying to use github repository to write. This is a read-only repository");
   }

   @Override
   protected RichContent preUpdate(RichContent n)
   {
      throw new RuntimeException("Trying to use github repository to write. This is a read-only repository");
   }

   @Override
   public RichContent fetch(Object key)
   {
      try
      {
         RichContent richContent = find(key);
         for (Image image : richContent.getImages())
         {
            image.getName();
         }
         em.clear();
         richContent.setContent(GithubImporter.getContent(richContent.getContent()));
         return richContent;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @PersistenceContext
   EntityManager em;

}
