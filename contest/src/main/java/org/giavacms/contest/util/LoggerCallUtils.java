package org.giavacms.contest.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

public class LoggerCallUtils
{

   static Logger logger = Logger.getLogger(LoggerCallUtils.class);

   public static Map<String, String> log(UriInfo uriInfo)
   {
      StringBuffer buffer = new StringBuffer();
      Map<String, String> map = new HashMap<>();
      MultivaluedMap<String, String> queryParams = uriInfo
               .getQueryParameters();
      MultivaluedMap<String, String> pathParams = uriInfo.getPathParameters();
      for (String key : queryParams.keySet())
      {
         String value = queryParams.getFirst(key);
         map.put(key, value);
         buffer.append(key + ": " + value + ";");
      }
      for (String key : pathParams.keySet())
      {
         String value = pathParams.getFirst(key);
         buffer.append(key + ": " + value + ";");
         map.put(key, value);
      }
      logger.info(buffer.toString());
      return map;
   }

   public static Map<String, String> log(ServletRequest servletRequest)
   {
      StringBuffer buffer = new StringBuffer();
      Map<String, String> map = new HashMap<>();
      Map<String, String[]> parameters = servletRequest.getParameterMap();
      for (String key : parameters.keySet())
      {
         String[] value = parameters.get(key);
         if (value != null && value.length > 0)
         {
            buffer.append(key + ": " + Arrays.toString(value) + ";");
            map.put(key, value[0]);
         }
      }
      logger.info(buffer.toString());
      return map;
   }
}
