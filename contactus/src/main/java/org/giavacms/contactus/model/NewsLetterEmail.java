/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.contactus.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NewsLetterEmail implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private boolean active = true;
   private String email;
   private String name;
   private Date data;

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

   public Date getData()
   {
      return data;
   }

   public void setData(Date data)
   {
      this.data = data;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Override
   public String toString()
   {
      return "NewsLetterEmail [id=" + id + ", active=" + active + ", name=" + name + ", email=" + email + ", data="
               + data + "]";
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

}
