/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.giavacms.base.model.Language;


public class I18nParams implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Map<String, String[]>[] maps = null;
   private Language[] languages;

   @SuppressWarnings("unchecked")
   public I18nParams(Language... languages)
   {
      maps = new Map[languages.length];
      this.languages = languages;
   }

   public Map<String, String[]>[] getAll()
   {
      return maps;
   }

   public Language[] getLanguages()
   {
      return this.languages;
   }

   public void put(int position, String name, String value)
   {
      puts(position, name, new String[] { value });
   }

   public void puts(int position, String name, String[] values)
   {
      Map<String, String[]> map = maps[position];
      if (map == null)
      {
         map = new HashMap<String, String[]>();
      }
      map.put(name, values);
      maps[position] = map;
   }

   public String get(int position, String name)
   {
      String[] values = gets(position, name);
      return values == null ? null : values.length == 0 ? null : values[0];
   }

   public String[] gets(int position, String name)
   {
      Map<String, String[]> map = maps[position];
      if (map == null)
      {
         return null;
      }
      return map.get(name);
   }

}
