package org.giavacms.banner.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Image;

@Entity
@Table(name = "Banner")
public class Banner implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private String name;
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

}
