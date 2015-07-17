package org.giavacms.contest.model.pojo;

import java.io.Serializable;

/**
 * Created by fiorenzo on 17/07/15.
 */
public class Login implements Serializable
{
   private String phone;

   public Login()
   {
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }
}
