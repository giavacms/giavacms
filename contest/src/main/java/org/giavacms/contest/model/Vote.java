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

   private String uuid;
   private String tocall;
   private Date created;
   private Date confirmed;

   private String name;
   private String surname;
   private String phone;

   private String preference1;
   private String preference2;
   private String preference3;

   private boolean sendMeParadeUpdates;
   private boolean sendMeFreeTickets;

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

   public Vote(String phone)
   {
      this.phone = phone;
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

   @Temporal(TemporalType.DATE)
   public Date getConfirmed()
   {
      return confirmed;
   }

   public void setConfirmed(Date confirmed)
   {
      this.confirmed = confirmed;
   }

   @Temporal(TemporalType.DATE)
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

   public String getPreference3()
   {
      return preference3;
   }

   public void setPreference3(String preference3)
   {
      this.preference3 = preference3;
   }

   public String getPreference1()
   {
      return preference1;
   }

   public void setPreference1(String preference1)
   {
      this.preference1 = preference1;
   }

   public String getPreference2()
   {
      return preference2;
   }

   public void setPreference2(String preference2)
   {
      this.preference2 = preference2;
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

   public boolean isSendMeFreeTickets()
   {
      return sendMeFreeTickets;
   }

   public void setSendMeFreeTickets(boolean sendMeFreeTickets)
   {
      this.sendMeFreeTickets = sendMeFreeTickets;
   }

   public boolean isSendMeParadeUpdates()
   {
      return sendMeParadeUpdates;
   }

   public void setSendMeParadeUpdates(boolean sendMeParadeUpdates)
   {
      this.sendMeParadeUpdates = sendMeParadeUpdates;
   }

   public String getTocall()
   {
      return tocall;
   }

   public void setTocall(String tocall)
   {
      this.tocall = tocall;
   }

   @Override public String toString()
   {
      return "Vote{" +
               "active=" + active +
               ", uuid='" + uuid + '\'' +
               ", tocall='" + tocall + '\'' +
               ", created=" + created +
               ", confirmed=" + confirmed +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", phone='" + phone + '\'' +
               ", preference1='" + preference1 + '\'' +
               ", preference2='" + preference2 + '\'' +
               ", preference3='" + preference3 + '\'' +
               ", sendMeParadeUpdates=" + sendMeParadeUpdates +
               ", sendMeFreeTickets=" + sendMeFreeTickets +
               '}';
   }
}
