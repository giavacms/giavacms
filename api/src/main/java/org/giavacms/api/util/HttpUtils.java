package org.giavacms.api.util;

import org.jboss.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class HttpUtils
{

   private static Logger logger = Logger.getLogger(HttpUtils.class);
   private static final String AUTHORIZATION_PROPERTY = "Authorization";
   private static final String AUTHORIZATION_PROPERTY_UPPER = "AUTHORIZATION";
   private static final String AUTHENTICATION_SCHEME = "Basic";
   private static final String AUTHENTICATION_SCHEME_UPPER = "BASIC";
   private static final String TOKEN_SCHEMA = "Bearer";
   private static final String TOKEN_SCHEMA_UPPER = "BEARER";

   public static String[] getUsernamePassword(HttpHeaders headers) throws Exception
   {
      if (headers.getRequestHeader(AUTHORIZATION_PROPERTY) != null)
      {
         return getUsernamePassword(headers.getRequestHeader(AUTHORIZATION_PROPERTY));
      }
      else if (headers.getRequestHeader(AUTHORIZATION_PROPERTY_UPPER) != null)
      {
         return getUsernamePassword(headers.getRequestHeader(AUTHORIZATION_PROPERTY_UPPER));
      }
      else
      {
         return null;
      }

   }

   public static String[] getUsernamePassword(MultivaluedMap<String, String> headers) throws Exception
   {
      return getUsernamePassword(headers.get(AUTHORIZATION_PROPERTY));
   }

   public static String getBearerToken(MultivaluedMap<String, String> headers) throws Exception
   {
      if (headers.containsKey(TOKEN_SCHEMA))
      {
         return headers.getFirst(TOKEN_SCHEMA);
      }
      else if (headers.containsKey(TOKEN_SCHEMA_UPPER))
      {
         return headers.getFirst(TOKEN_SCHEMA_UPPER);
      }
      else
      {
         return null;
      }
   }

   private static String[] getUsernamePassword(List<String> authorization) throws Exception
   {

      // If no authorization information present; block access
      if (authorization == null || authorization.isEmpty())
      {
         throw new Exception("authorization is empty");
      }

      // Get encoded username and password
      String encodedUserPassword = null;

      if (authorization.get(0).contains(AUTHENTICATION_SCHEME))
      {
         encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
      }
      else if (authorization.get(0).contains(AUTHENTICATION_SCHEME_UPPER))
      {
         encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME_UPPER + " ", "");
      }

      // Decode username and password
      String usernameAndPassword;
      try
      {
         usernameAndPassword = new String(Base64.decode(encodedUserPassword));
      }
      catch (IOException e)
      {
         throw new Exception("error in decoding username and password");
      }

      // Split username and password tokens
      try
      {
         final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
         final String username = tokenizer.nextToken();
         final String password = tokenizer.nextToken();

         // Verifying Username and password
         logger.info(username);
         System.out.println(password);
         return new String[] { username, password };
      }
      catch (Exception e)
      {
         throw new Exception("error in splitting username and password tokens");
      }

   }
}
