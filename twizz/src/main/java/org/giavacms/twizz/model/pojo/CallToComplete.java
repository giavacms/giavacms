package org.giavacms.twizz.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class CallToComplete implements Serializable
{
   private static final long serialVersionUID = 1L;
   private String callSid;
   private String phone;
   private Date when;

   public CallToComplete()
   {
   }

   public CallToComplete(String callSid, String phone, Date when)
   {
      this.callSid = callSid;
      this.phone = phone;
      this.when = when;
   }

   public String getCallSid()
   {
      return callSid;
   }

   public void setCallSid(String callSid)
   {
      this.callSid = callSid;
   }

   public Date getWhen()
   {
      return when;
   }

   public void setWhen(Date when)
   {
      this.when = when;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   @Override
   public String toString()
   {
      return "CallToComplete [callSid=" + callSid + ", phone=" + phone + ", when=" + when + "]";
   }

}
