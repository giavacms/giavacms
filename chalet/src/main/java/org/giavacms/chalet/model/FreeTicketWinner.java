package org.giavacms.chalet.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Entity
@Table(name = FreeTicketWinner.TABLE_NAME)
public class FreeTicketWinner implements Serializable
{
   public static final String TABLE_NAME = "freeticketwinners";
   private String uuid;
   private FreeTicket freeTicket;
   private String phonenumber;
   private String name;
   private String surname;

   public FreeTicketWinner()
   {
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

   @OneToOne
   public FreeTicket getFreeTicket()
   {
      return freeTicket;
   }

   public void setFreeTicket(FreeTicket freeTicket)
   {
      this.freeTicket = freeTicket;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getPhonenumber()
   {
      return phonenumber;
   }

   public void setPhonenumber(String phonenumber)
   {
      this.phonenumber = phonenumber;
   }

   public String getSurname()
   {
      return surname;
   }

   public void setSurname(String surname)
   {
      this.surname = surname;
   }
}
