package org.giavacms.chalet.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Entity
@Table(name = FreeTicket.TABLE_NAME)
public class FreeTicket implements Serializable
{
   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "freetickets";

   private String uuid;
   private Chalet chalet;
   private String description;
   private Date created;
   private Date usingTime;
   private Date consumed;

   public FreeTicket()
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

   @ManyToOne
   public Chalet getChalet()
   {
      return chalet;
   }

   public void setChalet(Chalet chalet)
   {
      this.chalet = chalet;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getConsumed()
   {
      return consumed;
   }

   public void setConsumed(Date consumed)
   {
      this.consumed = consumed;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getCreated()
   {
      return created;
   }

   public void setCreated(Date created)
   {
      this.created = created;
   }

   @Temporal(TemporalType.DATE)
   public Date getUsingTime()
   {
      return usingTime;
   }

   public void setUsingTime(Date usingTime)
   {
      this.usingTime = usingTime;
   }
}
