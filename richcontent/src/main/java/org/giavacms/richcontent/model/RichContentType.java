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

   private Long id;
   private boolean active = true;
   private String name;

   public RichContentType()
   {
   }

   public RichContentType(Long id)
   {
      this.id = id;
   }

   public RichContentType(String name)
   {
      this.name = name;
   }

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

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Override
   public String toString()
   {
      return "RichContentType [id=" + id + ", active=" + active + ", name="
               + name +
               "]";
   }

}
