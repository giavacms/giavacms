/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.util;

public class PlaceholderValorizzato
{
   private String key;
   private String value;

   public String getKey()
   {
      return key;
   }

   public PlaceholderValorizzato()
   {
   }

   public PlaceholderValorizzato(String key, String value)
   {
      this.key = key;
      this.value = value;
   }

   public void setKey(String key)
   {
      this.key = key;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

   @Override
   public String toString()
   {
      return "PlaceholderValorizzato [key=" + key + ", value=" + value + "]";
   }

}
