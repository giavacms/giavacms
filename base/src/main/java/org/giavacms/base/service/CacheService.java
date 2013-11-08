package org.giavacms.base.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageConfigurationRepository;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.model.Search;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class CacheService implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   FileSystemWriterService fileSystemWriterService;

   @Inject
   PageRepository pageRepository;

   @Inject
   PageConfigurationRepository pageConfigurationRepository;

   @Asynchronous
   public void cacheByPageId(String pageId)
   {
      if (pageConfigurationRepository.load().isWithCache())
         write(pageId);
   }

   @Asynchronous
   public void cacheByTemplateId(Long id)
   {
      if (pageConfigurationRepository.load().isWithCache())
         writeByTemplate(id);
   }

   @Asynchronous
   public void cacheByTemplateImplId(Long id)
   {
      if (pageConfigurationRepository.load().isWithCache())
         writeByTemplateImpl(id);
   }

   public String write(String pageId)
   {
      try
      {
         StringBuffer sb = new StringBuffer();
         Page page = pageRepository.fetch(pageId);
         boolean overwrite = true;
         List<String> files = fileSystemWriterService.write(page, overwrite);
         for (String file : files)
         {
            sb.append(", ").append(file);
         }
         return "Written: " + (sb.length() == 0 ? "none" : sb.substring(1));
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return e.getClass().getCanonicalName() + ": " + e.getMessage();
      }
   }

   public String writeAll()
   {
      Search<Page> sp = new Search<Page>(Page.class);
      sp.getObj().setExtended(true);
      sp.getObj().setClone(true);
      return writeAllWithSearch(sp, true);
   }

   public String writeByTemplate(Long id)
   {
      Search<Page> sp = new Search<Page>(Page.class);
      sp.getObj().getTemplate().getTemplate().setId(id);
      sp.getObj().setExtended(true);
      sp.getObj().setClone(true);
      return writeAllWithSearch(sp, true);
   }

   public String writeByTemplateImpl(Long id)
   {
      Search<Page> sp = new Search<Page>(Page.class);
      sp.getObj().getTemplate().setId(id);
      sp.getObj().setExtended(true);
      sp.getObj().setClone(true);
      return writeAllWithSearch(sp, true);
   }

   private String writeAllWithSearch(Search<Page> search, boolean overwrite)
   {
      try
      {
         StringBuffer sb = new StringBuffer();
         int pages = pageRepository.getListSize(search);
         int pagesPerIteration = 10;
         for (int i = 0; i < pages; i = i + pagesPerIteration)
         {
            for (Page page : pageRepository.getList(search, i, pagesPerIteration))
            {
               List<String> files = fileSystemWriterService.write(page, overwrite);
               for (String file : files)
               {
                  sb.append(", ").append(file);
               }
            }
            sb.append("---------------------------------------");
         }
         return "Written: " + (sb.length() == 0 ? "none" : sb.substring(1));
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return e.getClass().getCanonicalName() + ": " + e.getMessage();
      }
   }

   public String clear(
            String page)
   {
      try
      {
         return fileSystemWriterService.clear(page);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return e.getClass().getCanonicalName() + ": " + e.getMessage();
      }
   }

   public String clearAll()
   {
      try
      {
         String path = null;

         return fileSystemWriterService.clearAll(path);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return e.getClass().getCanonicalName() + ": " + e.getMessage();
      }
   }

   public String clearByTemplateId(Long id)
   {
      try
      {
         return "TODO";
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return e.getClass().getCanonicalName() + ": " + e.getMessage();
      }
   }

}
