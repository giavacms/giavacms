package org.giavacms.contest.util;

import org.giavacms.commons.jwt.management.JWTConstants;

import javax.servlet.ServletContext;

/**
 * Created by fiorenzo on 17/07/15.
 */
public class ServletContextUtils
{

   public static String getJwtSecret(ServletContext servletContext) throws Exception
   {
      String jwtSecret = JWTConstants.jwtSecret;
      String jwtSecretAsString = servletContext.getInitParameter(
               JWTConstants.JWT_SECRET_PROPERTY);
      if (jwtSecretAsString != null && !jwtSecretAsString.trim().isEmpty())
      {
         jwtSecret = jwtSecretAsString;
      }
      return jwtSecret;
   }

   public static int getJwtExpireTime(ServletContext servletContext) throws Exception
   {
      int jwtExpireTime = JWTConstants.jwtExpireTime;
      String jwtExpireTimeAsString = servletContext.getInitParameter(
               JWTConstants.JWT_EXPIRETIME_PROPERTY);
      if (jwtExpireTimeAsString != null && !jwtExpireTimeAsString.trim().isEmpty())
      {
         try
         {
            jwtExpireTime = Integer.parseInt(jwtExpireTimeAsString);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      return jwtExpireTime;
   }
}
