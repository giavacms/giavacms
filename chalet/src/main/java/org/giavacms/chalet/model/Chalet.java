package org.giavacms.chalet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.giavacms.api.annotation.Active;
import org.giavacms.base.model.attachment.Image;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = Chalet.TABLE_NAME)
@XmlRootElement
public class Chalet implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "chalets";
   public static final String TABLE_FK = "Chalet_id";
   public static final String IMAGES_JOINTABLE_NAME = "Chalet_Image";
   public static final String IMAGE_FK = "images_id";
   public static final String TAG_SEPARATOR = ",";

   private String id;
   private String name;
   private String licenseNumber;
   private String preview;
   private String description;
   private List<Image> images;
   private String tag;
   private List<String> tagList;
   private String tags;
   boolean active = true;

   private String owner;
   private String address;
   private String postalNumber;
   private String city;
   private String province;

   private String telephone;
   private String email;
   private String website;
   private String facebook;
   private String twitter;
   private String instagram;

   public Chalet()
   {
      super();
   }

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   @JsonIgnore
   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = IMAGES_JOINTABLE_NAME, joinColumns = @JoinColumn(name = TABLE_FK), inverseJoinColumns = @JoinColumn(name = IMAGE_FK))
   public List<Image> getImages()
   {
      if (this.images == null)
         this.images = new ArrayList<Image>();
      return images;
   }

   @Transient
   @JsonIgnore
   public Image getImage()
   {
      if (getImages() != null && getImages().size() > 0)
         return getImages().get(0);
      return null;
   }

   public void setImages(List<Image> images)
   {
      this.images = images;
   }

   public void addImage(Image image)
   {
      getImages().add(image);
   }

   @Transient
   @JsonIgnore
   public int getImgSize()
   {
      return getImages().size();
   }

   @Lob
   public String getPreview()
   {
      return preview;
   }

   public void setPreview(String preview)
   {
      this.preview = preview;
   }

   @Lob
   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getTags()
   {
      return tags;
   }

   public void setTags(String tags)
   {
      this.tags = tags;
      this.tagList = null;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getLicenseNumber()
   {
      return licenseNumber;
   }

   public void setLicenseNumber(String licenseNumber)
   {
      this.licenseNumber = licenseNumber;
   }

   @Active
   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Transient
   @JsonIgnore
   public String getTag()
   {
      return tag;
   }

   public void setTag(String tag)
   {
      this.tag = tag;
   }

   @Transient
   @JsonIgnore
   public List<String> getTagList()
   {
      if (tagList != null)
      {
         return tagList;
      }
      tagList = new ArrayList<String>();
      if (tags == null)
      {
         return tagList;
      }
      String[] tagArray = tags.split(TAG_SEPARATOR);
      for (String tag : tagArray)
      {
         if (tag != null && tag.trim().length() > 0)
         {
            tagList.add(tag.trim());
         }
      }
      return tagList;
   }

   public String getAddress()
   {
      return address;
   }

   public void setAddress(String address)
   {
      this.address = address;
   }

   public String getCity()
   {
      return city;
   }

   public void setCity(String city)
   {
      this.city = city;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public String getFacebook()
   {
      return facebook;
   }

   public void setFacebook(String facebook)
   {
      this.facebook = facebook;
   }

   public String getInstagram()
   {
      return instagram;
   }

   public void setInstagram(String instagram)
   {
      this.instagram = instagram;
   }

   public String getOwner()
   {
      return owner;
   }

   public void setOwner(String owner)
   {
      this.owner = owner;
   }

   public String getPostalNumber()
   {
      return postalNumber;
   }

   public void setPostalNumber(String postalNumber)
   {
      this.postalNumber = postalNumber;
   }

   public String getProvince()
   {
      return province;
   }

   public void setProvince(String province)
   {
      this.province = province;
   }

   public String getTwitter()
   {
      return twitter;
   }

   public void setTwitter(String twitter)
   {
      this.twitter = twitter;
   }

   public String getTelephone()
   {
      return telephone;
   }

   public void setTelephone(String telephone)
   {
      this.telephone = telephone;
   }

   public String getWebsite()
   {
      return website;
   }

   public void setWebsite(String website)
   {
      this.website = website;
   }

   @Override
   public String toString()
   {
      return "Chalet{" +
               "active=" + active +
               ", id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", licenseNumber='" + licenseNumber + '\'' +
               ", preview='" + preview + '\'' +
               ", description='" + description + '\'' +
               ", tag='" + tag + '\'' +
               ", tags='" + tags + '\'' +
               ", owner='" + owner + '\'' +
               ", address='" + address + '\'' +
               ", postalNumber='" + postalNumber + '\'' +
               ", city='" + city + '\'' +
               ", province='" + province + '\'' +
               ", telephone='" + telephone + '\'' +
               ", email='" + email + '\'' +
               ", website='" + website + '\'' +
               ", facebook='" + facebook + '\'' +
               ", twitter='" + twitter + '\'' +
               ", instagram='" + instagram + '\'' +
               '}';
   }
}
