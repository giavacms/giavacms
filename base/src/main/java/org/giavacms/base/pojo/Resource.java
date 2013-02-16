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

//@Entity
public class Resource implements Serializable
{

   private static final long serialVersionUID = 1L;

   // ------------------------------------------------------------------------

   private String id;
   private String type;
   private byte[] bytes;

   private String name;
   private InputStream inputStream;

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

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
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

}
