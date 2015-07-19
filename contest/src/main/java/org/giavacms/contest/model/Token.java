package org.giavacms.contest.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by fiorenzo on 17/07/15.
 */

@Entity
@Table(name = Token.TABLE_NAME)
public class Token implements Serializable
{
   private static final long serialVersionUID = -4581521841453347801L;
   public static final String TABLE_NAME = "Token";

   private String uuid;
   private String name;
   private String phone;
   private int duration = 0;
   private String token;
   private Date created;
   private Date confirmed;
   private Date destroyed;
   private String userRoles;

   public Token()
   {
   }

   public Token(Date created, String phone, String name, String userRoles)
   {
      this.created = created;
      this.phone = phone;
      this.name = name;
      this.userRoles = userRoles;
   }

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String getUuid()
   {
      return uuid;
   }

   public void setUuid(String uuid)
   {
      this.uuid = uuid;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public int getDuration()
   {
      return duration;
   }

   public void setDuration(int duration)
   {
      this.duration = duration;
   }

   @Column(length = 1024)
   public String getToken()
   {
      return token;
   }

   public void setToken(String token)
   {
      this.token = token;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   public Date getDestroyed()
   {
      return destroyed;
   }

   public void setDestroyed(Date destroyed)
   {
      this.destroyed = destroyed;
   }

   public Date getCreated()
   {
      return created;
   }

   public void setCreated(Date created)
   {
      this.created = created;
   }

   public Date getConfirmed()
   {
      return confirmed;
   }

   public void setConfirmed(Date confirmed)
   {
      this.confirmed = confirmed;
   }

   public String getUserRoles()
   {
      return userRoles;
   }

   public void setUserRoles(String userRoles)
   {
      this.userRoles = userRoles;
   }

   @Override public String toString()
   {
      return "Token{" +
               "confirmed=" + confirmed +
               ", uuid='" + uuid + '\'' +
               ", name='" + name + '\'' +
               ", phone='" + phone + '\'' +
               ", duration=" + duration +
               ", token='" + token + '\'' +
               ", created=" + created +
               ", destroyed=" + destroyed +
               ", userRoles='" + userRoles + '\'' +
               '}';
   }
}
