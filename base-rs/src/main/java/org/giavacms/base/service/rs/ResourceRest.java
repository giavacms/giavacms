/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.service.rs;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResourceRest implements Serializable
{

   private static final long serialVersionUID = 1L;

   private byte[] bytes;
   private String name;
   private String resourceType;

   public byte[] getBytes()
   {
      return bytes;
   }

   public void setBytes(byte[] bytes)
   {
      this.bytes = bytes;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getResourceType()
   {
      return resourceType;
   }

   public void setResourceType(String resourceType)
   {
      this.resourceType = resourceType;
   }

}
