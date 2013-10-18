package org.giavacms.base.service.rest;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.service.FileSystemWriterService;
import org.giavacms.common.model.Search;
import org.jboss.logging.Logger;

@Path("/v1/writer")
@Stateless
@LocalBean
public class FileSystemWriter implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   FileSystemWriterService fileSystemWriterService;

   @Inject
   PageRepository pageRepository;

   @GET
   @Path("/write/{page}")
   @Produces("text/plain")
   public String write(
            @PathParam("page") String pageId)
   {
      try
      {
         StringBuffer sb = new StringBuffer();
         boolean fetch = true;
         Page page = new Page();
         page.setId(pageId);
         boolean overwrite = true;
         String path = null;
         List<String> files = fileSystemWriterService.write(path, page, fetch, overwrite);
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

   @GET
   @Path("/writeAll")
   @Produces("text/plain")
   public String writeAll(
            )
   {
      try
      {
         StringBuffer sb = new StringBuffer();
         boolean fetch = true;
         boolean overwrite = false;
         String path = null;

         Search<Page> sp = new Search<Page>(Page.class);
         int pages = pageRepository.getListSize(sp);
         int pagesPerIteration = 10;
         for (int i = 0; i < pages; i = i + pagesPerIteration)
         {
            for (Page page : pageRepository.getList(new Search<Page>(Page.class), i, pagesPerIteration))
            {
               List<String> files = fileSystemWriterService.write(path, page, fetch, overwrite);
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

   @GET
   @Path("/clear/{page}")
   @Produces("text/plain")
   public String clear(
            @PathParam("page") String page)
   {
      try
      {
         String path = null;

         return fileSystemWriterService.clear(path, page);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return e.getClass().getCanonicalName() + ": " + e.getMessage();
      }
   }

   @GET
   @Path("/clearAll")
   @Produces("text/plain")
   public String clearAll(
            )
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

}
