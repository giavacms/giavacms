/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model.attachment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.enums.ResourceType;
import org.primefaces.model.UploadedFile;

@Entity
@Table(name = Image.TABLE_NAME)
public class Image implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String TABLE_NAME = "Image";

   private Long id;
   private boolean active = true;
   private String name;
   private String description;
   private String filename;
   private String filePath;
   private byte[] data;
   private String type;
   private UploadedFile uploadedData;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getFilename()
   {
      return filename;
   }

   public void setFilename(String filename)
   {
      this.filename = filename;
   }

   @Transient
   public byte[] getData()
   {
      return data;
   }

   public void setData(byte[] data)
   {
      this.data = data;
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
   public UploadedFile getUploadedData()
   {
      return uploadedData;
   }

   public void setUploadedData(UploadedFile uploadedData)
   {
      this.uploadedData = uploadedData;
   }

   @Transient
   public String getFilePath()
   {
      if (filePath == null)
      {
         filePath = ResourceType.IMAGE.getFolder() + "/" + filename;
      }
      return filePath;
   }

   public void setFilePath(String filepath)
   {
      this.filePath = filepath;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

}
