/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmailConfiguration implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private String pop;
   private String smtp;
   private String serverType;
   private int serverPort;
   private String username;
   private String password;
   private boolean withAuth;
   private boolean withSsl;
   private boolean withDebug;
   private String forwardMail;

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

   public String getPop()
   {
      return pop;
   }

   public void setPop(String pop)
   {
      this.pop = pop;
   }

   public String getSmtp()
   {
      return smtp;
   }

   public void setSmtp(String smtp)
   {
      this.smtp = smtp;
   }

   public int getServerPort()
   {
      return serverPort;
   }

   public void setServerPort(int serverPort)
   {
      this.serverPort = serverPort;
   }

   public String getServerType()
   {
      return serverType;
   }

   public void setServerType(String serverType)
   {
      this.serverType = serverType;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public boolean isWithAuth()
   {
      return withAuth;
   }

   public void setWithAuth(boolean withAuth)
   {
      this.withAuth = withAuth;
   }

   public boolean isWithSsl()
   {
      return withSsl;
   }

   public void setWithSsl(boolean withSsl)
   {
      this.withSsl = withSsl;
   }

   public boolean isWithDebug()
   {
      return withDebug;
   }

   public void setWithDebug(boolean withDebug)
   {
      this.withDebug = withDebug;
   }

   @Override
   public String toString()
   {
      return "EmailConfiguration [id=" + id + ", pop=" + pop + ", smtp="
               + smtp + ", serverType=" + serverType + ", serverPort="
               + serverPort + ", username=" + username + ", password="
               + password + ", withAuth=" + withAuth + ", withSsl=" + withSsl
               + ", withDebug=" + withDebug + "]";
   }

   public String getForwardMail()
   {
      return forwardMail;
   }

   public void setForwardMail(String forwardMail)
   {
      this.forwardMail = forwardMail;
   }

}