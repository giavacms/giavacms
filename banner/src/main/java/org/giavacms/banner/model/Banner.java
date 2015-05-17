package org.giavacms.banner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.giavacms.base.model.attachment.Image;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = Banner.TABLE_NAME)
public class Banner implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Banner";

   private Long id;
   private String name;
   private String language;
   private BannerTypology bannerTypology;
   private boolean active = true;
   private String url;
   private boolean internal;
   private String description;
   private Image image;
   private Image newImage;
   private boolean online;

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

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
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

   @ManyToOne
   public BannerTypology getBannerTypology()
   {
      if (this.bannerTypology == null)
         this.bannerTypology = new BannerTypology();
      return bannerTypology;
   }

   public void setBannerTypology(BannerTypology bannerTypology)
   {
      this.bannerTypology = bannerTypology;
   }

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "image_id", unique = true, nullable = true, insertable = true, updatable = true)
   public Image getImage()
   {
      if (image == null)
         this.image = new Image();
      return image;
   }

   public void setImage(Image image)
   {
      this.image = image;
   }

   @Transient
   @JsonIgnore
   public Image getNewImage()
   {
      if (newImage == null)
         this.newImage = new Image();
      return newImage;
   }

   public void setNewImage(Image newImage)
   {
      this.newImage = newImage;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }

   public boolean isInternal()
   {
      return internal;
   }

   public void setInternal(boolean internal)
   {
      this.internal = internal;
   }

   @Lob
   @Basic(fetch = FetchType.EAGER)
   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public boolean isOnline()
   {
      return online;
   }

   public void setOnline(boolean online)
   {
      this.online = online;
   }

   @Override public String toString()
   {
      return "Banner{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", language='" + language + '\'' +
               ", bannerTypology=" + bannerTypology +
               ", active=" + active +
               ", url='" + url + '\'' +
               ", internal=" + internal +
               ", description='" + description + '\'' +
               ", image=" + image +
               ", online=" + online +
               '}';
   }
}
