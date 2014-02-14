/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.service.rs;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateImplRepository;
import org.jboss.logging.Logger;

@Path("/v1/base10importer")
@Stateless
@LocalBean
public class Base10importerRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   TemplateImplRepository templateImplRepository;
   @Inject
   PageRepository pageRepository;

   @GET
   @Path("/importOne/{templateImplId}")
   @Produces("text/plain")
   public String runOne(
            @PathParam("templateImplId") Long templateImplId)
   {
      TemplateImpl templateImpl = templateImplRepository.find(templateImplId);
      if (templateImpl == null)
      {
         return "NO TEMPLATE_IMPL WITH ID #" + templateImplId;
      }
      if (templateImpl.getMainPageId() == null)
      {
         return "TEMPLATE_IMPL WITH ID #" + templateImplId + " HAS NO MAINPAGE";
      }
      Page page = pageRepository.find(templateImpl.getMainPageId());
      if (page == null)
      {
         return "NO PAGE WITH ID #" + templateImpl.getMainPageId();
      }
      templateImpl.setMainPageTitle(page.getTitle());
      if (templateImplRepository.update(templateImpl))
      {
         return "TEMPLATE_IMPL WITH ID #" + templateImplId + " AND MAINPAGE ID '" + templateImpl.getMainPageId()
                  + "' HAS BEEN CORRECTLY ASSOCIATED TO MAINPAGE_TITLE '" + templateImpl.getMainPageTitle();
      }
      else
      {
         return "TEMPLATE_IMPL WITH ID #" + templateImplId + " AND MAINPAGE ID '" + templateImpl.getMainPageId()
                  + "' COULD NOT BE ASSSOCIATED WITH MAINPAGE_TITLE '" + templateImpl.getMainPageTitle();
      }
   }

   @GET
   @Path("/import")
   @Produces("text/plain")
   public String importAll()
   {
      StringBuffer sb = new StringBuffer();
      sb.append(templateImplRepository.resetMainPageTitles() + " titoli di pagina base resettati ").append(" | ");
      sb.append(templateImplRepository.makeMainPageTitles() + " titoli di pagina base reimpostati ").append(" | ");
      sb.append(templateImplRepository.cleanMainPageIds() + " identificativi di pagina base ripuliti ").append(" | ");
      return sb.toString();

   }
}
