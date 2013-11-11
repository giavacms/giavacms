package org.giavacms.cache.service.rs;


import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.giavacms.cache.service.CacheService;
import org.jboss.logging.Logger;

@Path("/v1/writer")
@Stateless
@LocalBean
public class FileSystemWriter implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   CacheService cahCacheService;

   @GET
   @Path("/write/{page}")
   @Produces("text/plain")
   public String write(
            @PathParam("page") String pageId)
   {
      return cahCacheService.write(pageId);
   }

   @GET
   @Path("/writeAll")
   @Produces("text/plain")
   public String writeAll()
   {
      return cahCacheService.writeAll();
   }

   @GET
   @Path("/writeByTemplateId/{id}")
   @Produces("text/plain")
   public String writeByTemplate(@PathParam("id") Long id)
   {

      return cahCacheService.writeByTemplate(id);
   }

   @GET
   @Path("/clear/{page}")
   @Produces("text/plain")
   public String clear(
            @PathParam("page") String page)
   {
      return cahCacheService.clear(page);
   }

   @GET
   @Path("/clearAll")
   @Produces("text/plain")
   public String clearAll(
            )
   {
      return cahCacheService.clearAll();
   }

   @GET
   @Path("/clearByTemplateId/{id}")
   @Produces("text/plain")
   public String clearByTemplateId(
            @PathParam("id") Long id)
   {
      return cahCacheService.clearByTemplateId(id);
   }

}
