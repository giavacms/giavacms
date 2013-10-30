package org.giavacms.contactus.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ContactUsConfiguration implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private boolean active = true;
   private boolean newsletter;
   private boolean contactus;
   private boolean from;
   private boolean to;
   private boolean cc;
   private boolean bcc;
   private String email;
   private String description;

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

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Column(name="TO_USAGE")
   public boolean isTo()
   {
      return to;
   }

   public void setTo(boolean to)
   {
      this.to = to;
   }

   @Column(name="CC_USAGE")
   public boolean isCc()
   {
      return cc;
   }

   public void setCc(boolean cc)
   {
      this.cc = cc;
   }

   @Column(name="BCC_USAGE")
   public boolean isBcc()
   {
      return bcc;
   }

   public void setBcc(boolean bcc)
   {
      this.bcc = bcc;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   @Column(name="FROM_USAGE")
   public boolean isFrom()
   {
      return from;
   }

   public void setFrom(boolean from)
   {
      this.from = from;
   }

   public boolean isNewsletter()
   {
      return newsletter;
   }

   public void setNewsletter(boolean newsletter)
   {
      this.newsletter = newsletter;
   }

   public boolean isContactus()
   {
      return contactus;
   }

   public void setContactus(boolean contactus)
   {
      this.contactus = contactus;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

}
