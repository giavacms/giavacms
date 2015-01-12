/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.pojo;

import java.io.InputStream;
import java.io.Serializable;

import javax.persistence.Transient;

import org.giavacms.base.model.enums.ResourceType;

//@Entity
public class Resource implements Serializable
{

   private static final long serialVersionUID = 1L;

   // ------------------------------------------------------------------------

   private String id;
   private byte[] bytes;
   private boolean exists;

   private String name;
   private InputStream inputStream;
   private ResourceType resourceType;
   
   private String type;

   // ------------------------------------------------------------------------

   public Resource()
   {
   }

   // ------------------------------------------------------------------------

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   @Transient
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public ResourceType getResourceType()
   {
      return resourceType;
   }

   public void setResourceType(ResourceType resourceType)
   {
      this.resourceType = resourceType;
   }

   public String getType()
   {
      return type != null ? type : resourceType != null ? resourceType.getFolder() : null;
   }

   public void setType(String type)
   {
      for (ResourceType resourceType : ResourceType.values())
      {
         if (resourceType.getFolder().equals(type))
         {
            this.resourceType = resourceType;
            this.type = null;
            return;
         }
      }
      this.type = type;
   }

   @Transient
   public byte[] getBytes()
   {
      return bytes;
   }

   public void setBytes(byte[] bytes)
   {
      this.bytes = bytes;
   }

   @Transient
   public String getText()
   {
      return new String(bytes);
   }

   public void setText(String text)
   {
      this.bytes = text.getBytes();
   }

   @Transient
   public int getSize()
   {
      return bytes == null ? 0 : bytes.length;
   }

   // ------------------------------------------------------------------------

   @Override
   public String toString()
   {
      return (this.id != null) ? this.id.toString() : super.toString();
   }

   @Transient
   public InputStream getInputStream()
   {
      return inputStream;
   }

   public void setInputStream(InputStream inputStream)
   {
      this.inputStream = inputStream;
   }

   @Transient
   public boolean isExists()
   {
      return exists;
   }

   public void setExists(boolean exists)
   {
      this.exists = exists;
   }

   
}
