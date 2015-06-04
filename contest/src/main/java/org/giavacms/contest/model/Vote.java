package org.giavacms.contest.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by fiorenzo on 04/06/15.
 */
@Entity
@Table(name = Vote.TABLE_NAME)

public class Vote implements Serializable
{
   private static final long serialVersionUID = -4581521841453347801L;
   public static final String TABLE_NAME = "Vote";

   private String uid;
   private Date created;
   private Date confirmed;

   private String name;
   private String surname;
   private String phone;

   private String picture;
   private String sculpture;
   private String photo;

   private boolean active;

   public Vote()
   {
   }

   public Vote(String surname, String name, String phone)
   {
      this.surname = surname;
      this.name = name;
      this.phone = phone;
   }

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String getUid()
   {
      return uid;
   }

   public void setUid(String uid)
   {
      this.uid = uid;
   }

   public Date getConfirmed()
   {
      return confirmed;
   }

   public void setConfirmed(Date confirmed)
   {
      this.confirmed = confirmed;
   }

   public Date getCreated()
   {
      return created;
   }

   public void setCreated(Date created)
   {
      this.created = created;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   public String getPhoto()
   {
      return photo;
   }

   public void setPhoto(String photo)
   {
      this.photo = photo;
   }

   public String getPicture()
   {
      return picture;
   }

   public void setPicture(String picture)
   {
      this.picture = picture;
   }

   public String getSculpture()
   {
      return sculpture;
   }

   public void setSculpture(String sculpture)
   {
      this.sculpture = sculpture;
   }

   public String getSurname()
   {
      return surname;
   }

   public void setSurname(String surname)
   {
      this.surname = surname;
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
