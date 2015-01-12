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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.pojo.Resource;
import org.giavacms.base.repository.ResourceRepository;
import org.giavacms.common.util.FileUtils;
import org.jboss.logging.Logger;

@Path("/v1/resource")
@Stateless
@LocalBean
public class ResourceUploader implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   ResourceRepository resourceRepository;

   @PUT
   @Path("/upload")
   @Produces(MediaType.TEXT_PLAIN)
   @Consumes(MediaType.APPLICATION_XML)
   public String upload(ResourceRest resourceRest)
   {
      Resource resource = new Resource();
      resource.setBytes(resourceRest.getBytes());
      resource.setName(FileUtils.cleanName(FileUtils.getLastPartOf(resourceRest.getName())));
      resource.setResourceType(ResourceType.valueOf(resourceRest.getResourceType()));
      return resourceRepository.persist(resource).getId();
   }

   @PUT
   @Path("/overwrite")
   @Produces(MediaType.TEXT_PLAIN)
   @Consumes(MediaType.APPLICATION_XML)
   public String overwrite(ResourceRest resourceRest)
   {
      Resource resource = new Resource();
      resource.setBytes(resourceRest.getBytes());
      resource.setName(resourceRest.getName());
      resource.setResourceType(ResourceType.valueOf(resourceRest.getResourceType()));
      return resourceRepository.updateResource(resource).getId();
   }

   @GET
   @Path("/download")
   @Produces(MediaType.WILDCARD)
   public byte[] overwrite(@QueryParam("type") String type, @QueryParam("id") String id)
   {
      return resourceRepository.fetch(type, id).getBytes();
   }

}
