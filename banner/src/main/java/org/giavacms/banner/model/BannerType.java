package org.giavacms.banner.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = BannerType.TABLE_NAME)
public class BannerType implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "BannerType";

   private Long id;
   private String name;
   private String description;
   private String type;
   private boolean active = true;
   private String language;

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

   @Lob
   @Column(length = 100 * 1024)
   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
   }

   @Override public String toString()
   {
      return "BannerType{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", type='" + type + '\'' +
               ", active=" + active +
               ", language='" + language + '\'' +
               '}';
   }
}
