package org.giavacms.exhibition.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.giavacms.common.util.StringUtils;
import org.giavacms.exhibition.enums.ParticipationType;

@Entity
@Table(name = "ExhibitionParticipant")
public class Participant implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private Exhibition exhibition;
   private Subject subject;
   private String description;

   private String artistName;

   private String address;
   private String city;
   private String province;

   private String phone;
   private String email;

   private String oeuvreTitle;
   private String dimensions;
   private String material;
   private String summary;

   private Discipline discipline;
   private ParticipationType participationType;

   private String webSite;
   private String facebookAccount;
   private String twitterAccount;
   private String instagramAccount;
   private Image image;
   private Image newImage;
   private String externalImage;
   private String date;
   private boolean active = true;

   private boolean toControl = false;

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

   @ManyToOne
   public Subject getSubject()
   {
      if (subject == null)
         this.subject = new Subject();
      return subject;
   }

   public void setSubject(Subject subject)
   {
      this.subject = subject;
   }

   @ManyToOne
   public Exhibition getExhibition()
   {
      if (exhibition == null)
         this.exhibition = new Exhibition();
      return exhibition;
   }

   public void setExhibition(Exhibition exhibition)
   {
      this.exhibition = exhibition;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public String getDimensions()
   {
      return dimensions;
   }

   public void setDimensions(String dimensions)
   {
      this.dimensions = dimensions;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getMaterial()
   {
      return material;
   }

   public void setMaterial(String material)
   {
      this.material = material;
   }

   @ManyToOne(optional = true)
   @JoinColumn(name = "Discipline_id", nullable = true)
   public Discipline getDiscipline()
   {
      if (discipline == null)
         this.discipline = new Discipline();
      return discipline;
   }

   public void setDiscipline(Discipline discipline)
   {
      this.discipline = discipline;
   }

   public String getFacebookAccount()
   {
      return facebookAccount;
   }

   public void setFacebookAccount(String facebookAccount)
   {
      this.facebookAccount = facebookAccount;
   }

   public String getInstagramAccount()
   {
      return instagramAccount;
   }

   public void setInstagramAccount(String instagramAccount)
   {
      this.instagramAccount = instagramAccount;
   }

   public String getExternalImage()
   {
      return externalImage;
   }

   public void setExternalImage(String externalImage)
   {
      this.externalImage = externalImage;
   }

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "image_id", unique = true, nullable = true, insertable = true, updatable = true)
   public Image getImage()
   {
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

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   public String getOeuvreTitle()
   {
      return oeuvreTitle;
   }

   public void setOeuvreTitle(String oeuvreTitle)
   {
      this.oeuvreTitle = oeuvreTitle;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getSummary()
   {
      return summary;
   }

   public void setSummary(String summary)
   {
      this.summary = summary;
   }

   @Enumerated(EnumType.STRING)
   public ParticipationType getParticipationType()
   {
      return participationType;
   }

   public void setParticipationType(ParticipationType participationType)
   {
      this.participationType = participationType;
   }

   public String getWebSite()
   {
      return webSite;
   }

   public void setWebSite(String webSite)
   {
      this.webSite = webSite;
   }

   public String getTwitterAccount()
   {
      return twitterAccount;
   }

   public void setTwitterAccount(String twitterAccount)
   {
      this.twitterAccount = twitterAccount;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public String getDate()
   {
      return date;
   }

   public void setDate(String date)
   {
      this.date = date;
   }

   @Lob
   public String getDescription()
   {
      return description;
   }

   @Transient
   public String getPreview()
   {
      if (description != null && description.length() > 200)
      {
         String noHTMLString = StringUtils.trim(description.replaceAll("\\<.*?\\>", ""), 200);
         return noHTMLString;
      }
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
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

   public String getProvince()
   {
      return province;
   }

   public void setProvince(String province)
   {
      this.province = province;
   }

   @Transient
   public String getArtistName()
   {
      return artistName;
   }

   public void setArtistName(String artistName)
   {
      this.artistName = artistName;
   }

   @Override
   public String toString()
   {
      return "Participant [id=" + id + ", exhibition=" + exhibition.getName()
               + ", subject=" + subject.getName() + ", description="
               + description + ", phone=" + phone + ", email=" + email
               + ", oeuvreTitle=" + oeuvreTitle + ", dimensions=" + dimensions
               + ", material=" + material + ", summary=" + summary
               + ", discipline=" + discipline + ", participationType="
               + participationType + ", webSite=" + webSite
               + ", facebookAccount=" + facebookAccount + ", twitterAccount="
               + twitterAccount + ", instagramAccount=" + instagramAccount
               + ", date=" + date + "]";
   }

   public boolean isToControl()
   {
      return toControl;
   }

   public void setToControl(boolean toControl)
   {
      this.toControl = toControl;
   }

}
