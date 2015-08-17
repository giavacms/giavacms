package org.giavacms.base.management;

import org.giavacms.base.util.SystemPropertiesUtils;

public enum AppProperties
{

   authId, authToken,

   number, url,

   skebbyNumber, skebbyUsername, skebbyPassword,

   imageResizeCommand,

   accountEmailUser, accountEmailPassword, photoAlertEmailFrom, photoAlertEmailTo, mailHost, mailPort, AppProperties;

   @SuppressWarnings("unchecked")
   public <T> T cast(Class<T> clazz)
   {
      if (Long.class.equals(clazz))
      {
         return (T) new Long(value());
      }
      if (Integer.class.equals(clazz))
      {
         return (T) new Integer(value());
      }
      if (Boolean.class.equals(clazz))
      {
         return (T) new Boolean(value());
      }
      return (T) value();
   }

   public String value()
   {
      String value = SystemPropertiesUtils.getProperty(AppConstants.APP_NAME,
               name());
      if (value == null)
      {
         throw new RuntimeException("Missing configuration for: " + name());
      }
      return value;
   }

   public String value(boolean optional)
   {
      String value = SystemPropertiesUtils.getProperty(AppConstants.APP_NAME,
               name());
      if (!optional && value == null)
      {
         throw new RuntimeException("Missing configuration for: " + name());
      }
      return value;
   }

   public String replace(String[] placeholders, Object[] values)
   {
      String value = value();
      if (placeholders != null && values != null
               && placeholders.length == values.length)
      {
         for (int i = 0; i < placeholders.length; i++)
         {
            value = value.replaceAll("\\{" + placeholders[i] + "\\}",
                     (values[i] == null ? "" : values[i].toString()));
         }
      }
      return value;
   }

   public String[] split(String string)
   {
      return value().split(string);
   }

}
