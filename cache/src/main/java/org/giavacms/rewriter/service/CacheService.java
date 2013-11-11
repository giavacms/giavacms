package org.giavacms.rewriter.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.filter.MappingFilter;
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

   @Asynchronous
   public void cacheByPageId(String pageId)
   {
      write(pageId);
   }

   @Asynchronous
   public void cacheByTemplateId(Long id)
   {
      writeByTemplate(id);
   }

   @Asynchronous
   public void cacheByTemplateImplId(Long id)
   {
      writeByTemplateImpl(id);
   }

   public String write(String pageId)
   {
      try
      {
         StringBuffer sb = new StringBuffer();
         Page page = pageRepository.fetch(pageId);
         boolean overwrite = true;
         List<String> files = fileSystemWriterService.write(getAbsolutePath(), page, overwrite);
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
      return writeAllWithSearch(null, sp, true);
   }

   public String writeByTemplate(Long id)
   {
      Search<Page> sp = new Search<Page>(Page.class);
      sp.getObj().getTemplate().getTemplate().setId(id);
      sp.getObj().setExtended(true);
      sp.getObj().setClone(true);
      return writeAllWithSearch(null, sp, true);
   }

   public String writeByTemplateImpl(Long id)
   {
      Search<Page> sp = new Search<Page>(Page.class);
      sp.getObj().getTemplate().setId(id);
      sp.getObj().setExtended(true);
      sp.getObj().setClone(true);
      return writeAllWithSearch(null, sp, true);
   }

   private String writeAllWithSearch(String path, Search<Page> search, boolean overwrite)
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
               List<String> files = fileSystemWriterService.write(getAbsolutePath(path), page, overwrite);
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
         return fileSystemWriterService.clear(getAbsolutePath(), page);
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
         return fileSystemWriterService.clearAll(getAbsolutePath());
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

   @Asynchronous
   public void writeAll(String path)
   {
      Search<Page> sp = new Search<Page>(Page.class);
      sp.getObj().setExtended(true);
      sp.getObj().setClone(true);
      writeAllWithSearch(path, sp, true);
   }

   private File getAbsolutePath() throws Exception
   {
      return getAbsolutePath(null);
   }

   private File getAbsolutePath(String path) throws Exception
   {
      File absolutePath = null;
      if (path == null || path.trim().length() == 0)
      {
         String realPath = getClass().getClassLoader().getResource("cache.marker").getPath();
         ServletContext servletContext = (ServletContext) FacesContext
                  .getCurrentInstance().getExternalContext().getContext();
         absolutePath = new File(realPath.substring(0, realPath.indexOf("WEB-INF")),
                  servletContext.getInitParameter(MappingFilter.PAGES_PATH_PARAM_NAME));
      }
      else
      {
         String realPath = getClass().getClassLoader().getResource("cache.marker").getPath();
         absolutePath = new File(realPath.substring(0, realPath.indexOf("WEB-INF")), path.replace("/", ""));
      }
      if (!absolutePath.exists())
      {
         if (absolutePath.mkdir())
         {
            return absolutePath;
         }
         else
         {
            throw new Exception("Failed to make dir: " + path);
         }
      }
      else
      {
         if (!absolutePath.isDirectory())
         {
            throw new Exception("Invalid dir: " + path);
         }
         else
         {
            return absolutePath;
         }
      }
   }

   public void cacheByPageIdAndTemplateId(String pageId, boolean clone, Long templateId)
   {
      cacheByPageId(pageId);
      if (!clone)
      {
         cacheByTemplateImplId(templateId);
      }
   }
}
