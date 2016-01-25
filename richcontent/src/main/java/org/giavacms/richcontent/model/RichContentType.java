package org.giavacms.richcontent.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@Entity
@Table(name = RichContentType.TABLE_NAME)
@XmlRootElement
public class RichContentType implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "RichContentType";

   private String id;
   private boolean active = true;
   private String name;
   private boolean resize;
   private int maxWidthOrHeight;

   public RichContentType()
   {
   }

   public RichContentType(String name)
   {
      this.name = name;
   }

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
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

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public int getMaxWidthOrHeight()
   {
      return maxWidthOrHeight;
   }

   public void setMaxWidthOrHeight(int maxWidthOrHeight)
   {
      this.maxWidthOrHeight = maxWidthOrHeight;
   }

   public boolean isResize()
   {
      return resize;
   }

   public void setResize(boolean resize)
   {
      this.resize = resize;
   }

   @Override
   public String toString()
   {
      return "RichContentType{" +
               "active=" + active +
               ", id=" + id +
               ", name='" + name + '\'' +
               ", resize=" + resize +
               ", maxWidthOrHeight=" + maxWidthOrHeight +
               '}';
   }
}
