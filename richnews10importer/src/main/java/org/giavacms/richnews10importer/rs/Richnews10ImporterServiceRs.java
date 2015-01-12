package org.giavacms.richnews10importer.rs;

/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.giavacms.richnews10importer.service.Richnews10ImporterService;
import org.jboss.logging.Logger;

@Path("/v1/richnews10importer")
@Stateless
@LocalBean
public class Richnews10ImporterServiceRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   Richnews10ImporterService richnews10ImporterService;

   @GET
   @Path("/import")
   @Produces(MediaType.APPLICATION_JSON)
   public String importAll()
   {
      try
      {
         richnews10ImporterService.doImport();
         return "OK";
      }
      catch (Throwable t)
      {
         t.printStackTrace();
         return "KO";
      }
   }
}
