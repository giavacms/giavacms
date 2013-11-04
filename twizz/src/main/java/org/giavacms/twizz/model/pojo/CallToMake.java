package org.giavacms.twizz.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class CallToMake implements
         Serializable
{
   private static final long serialVersionUID = 1L;
   private String to;
   private String url;
   private Date when;

   public CallToMake()
   {
   }

   public CallToMake(String to, String url, Date when)
   {
      this.to = to;
      this.url = url;
      this.when = when;
   }

   public String getTo()
   {
      return to;
   }

   public void setTo(String to)
   {
      this.to = to;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }

   public Date getWhen()
   {
      return when;
   }

   public void setWhen(Date when)
   {
      this.when = when;
   }

   @Override
   public String toString()
   {
      return "CallToMake [to=" + to + ", url=" + url + ", when=" + when + "]";
   }

}
