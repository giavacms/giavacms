package org.giavacms.expo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

/**
 * Created by fiorenzo on 04/06/15.
 */
@Entity
public class Artist implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String id;
   private String name;
   private String surname;
   private String stagename;
   private String telephone;
   private String email;
   private String imageurl;
   private String website;
   private String facebook;
   private String twitter;
   private String instagram;

   private String biography;

   private List<Participation> participations;

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

   public String getSurname()
   {
      return surname;
   }

   public void setSurname(String surname)
   {
      this.surname = surname;
   }

   public String getStagename()
   {
      return stagename;
   }

   public void setStagename(String stagename)
   {
      this.stagename = stagename;
   }

   public String getTelephone()
   {
      return telephone;
   }

   public void setTelephone(String telephone)
   {
      this.telephone = telephone;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public String getWebsite()
   {
      return website;
   }

   public void setWebsite(String website)
   {
      this.website = website;
   }

   public String getFacebook()
   {
      return facebook;
   }

   public void setFacebook(String facebook)
   {
      this.facebook = facebook;
   }

   public String getTwitter()
   {
      return twitter;
   }

   public void setTwitter(String twitter)
   {
      this.twitter = twitter;
   }

   public String getInstagram()
   {
      return instagram;
   }

   public void setInstagram(String instagram)
   {
      this.instagram = instagram;
   }

   @Lob
   @Column(length = 1024 * 10)
   public String getBiography()
   {
      return biography;
   }

   public void setBiography(String biography)
   {
      this.biography = biography;
   }

   @OneToMany(mappedBy = "artist")
   public List<Participation> getParticipations()
   {
      return participations;
   }

   public void setParticipations(List<Participation> participations)
   {
      this.participations = participations;
   }

   @Override
   public String toString()
   {
      return "Artist [id=" + id + ", name=" + name + ", surname=" + surname + ", stagename=" + stagename
               + ", telephone=" + telephone + ", email=" + email + ", website=" + website + ", facebook=" + facebook
               + ", twitter=" + twitter + ", imageurl=" + imageurl + ", instagram=" + instagram + ", biography="
               + biography + "]";
   }

}
