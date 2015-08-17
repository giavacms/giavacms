package org.giavacms.chalet.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by fiorenzo on 31/07/15.
 */
@Entity
@Table(name = Photo.TABLE_NAME)
public class Photo implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "photos";

   private String uuid;
   private String name;
   private String chaletId;
   private String accountId;
   private String accountUuid;
   private String accountNameSurname;
   private String accountNameSurnameShort;
   private String chaletName;
   private Date created;
   private boolean approved = false;
   private Date approvedDate;
   private boolean active = false;
   private int ranking;

   public Photo()
   {
   }

   @Id
   public String getUuid()
   {
      return uuid;
   }

   public void setUuid(String uuid)
   {
      this.uuid = uuid;
   }

   public String getAccountId()
   {
      return accountId;
   }

   public void setAccountId(String accountId)
   {
      this.accountId = accountId;
   }

   public boolean isApproved()
   {
      return approved;
   }

   public void setApproved(boolean approved)
   {
      this.approved = approved;
   }

   public String getChaletId()
   {
      return chaletId;
   }

   public void setChaletId(String chaletId)
   {
      this.chaletId = chaletId;
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

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public int getRanking()
   {
      return ranking;
   }

   public void setRanking(int ranking)
   {
      this.ranking = ranking;
   }

   public String getChaletName()
   {
      return chaletName;
   }

   public void setChaletName(String chaletName)
   {
      this.chaletName = chaletName;
   }

   public String getAccountNameSurname()
   {
      return accountNameSurname;
   }

   public void setAccountNameSurname(String accountNameSurname)
   {
      this.accountNameSurname = accountNameSurname;
   }

   public Date getApprovedDate()
   {
      return approvedDate;
   }

   public void setApprovedDate(Date approvedDate)
   {
      this.approvedDate = approvedDate;
   }

   public String getAccountNameSurnameShort()
   {
      return accountNameSurnameShort;
   }

   public void setAccountNameSurnameShort(String accountNameSurnameShort)
   {
      this.accountNameSurnameShort = accountNameSurnameShort;
   }

   public String getAccountUuid()
   {
      return accountUuid;
   }

   public void setAccountUuid(String accountUuid)
   {
      this.accountUuid = accountUuid;
   }
}
