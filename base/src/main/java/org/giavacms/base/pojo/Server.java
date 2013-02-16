/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.pojo;

import java.io.Serializable;

public class Server implements Serializable
{

   private static final long serialVersionUID = 1L;
   private String address;
   private String user;
   private String pwd;
   private boolean auth;
   private boolean ssl;
   private String port;
   private boolean debug;

   public String getAddress()
   {
      return address;
   }

   public void setAddress(String address)
   {
      this.address = address;
   }

   public boolean isAuth()
   {
      return auth;
   }

   public void setAuth(boolean auth)
   {
      this.auth = auth;
   }

   public String getPwd()
   {
      return pwd;
   }

   public void setPwd(String pwd)
   {
      this.pwd = pwd;
   }

   public String getUser()
   {
      return user;
   }

   public void setUser(String user)
   {
      this.user = user;
   }

   public String getPort()
   {
      return port;
   }

   public void setPort(String port)
   {
      this.port = port;
   }

   public boolean isSsl()
   {
      return ssl;
   }

   public void setSsl(boolean ssl)
   {
      this.ssl = ssl;
   }

   public boolean isDebug()
   {
      return debug;
   }

   public void setDebug(boolean debug)
   {
      this.debug = debug;
   }
}
