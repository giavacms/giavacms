package org.giava.cms.base.rs;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class AdminAdminAuthInterceptor implements HttpRequestInterceptor
{

   @Override
   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
   {
      String val = (new StringBuffer("admin").append(":").append("admin")).toString();
      String authorizationString = "Basic " + new String(Base64.encodeBase64(val.getBytes()));
      request.setHeader("Authorization", authorizationString);
   }

   public static void main(String[] args)
   {
      String val = (new StringBuffer("admin").append(":").append("admin")).toString();
      String authorizationString = "Basic " + new String(Base64.encodeBase64(val.getBytes()));
      System.out.println(authorizationString);
   }
}
