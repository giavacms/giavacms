/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class I18nSupport implements I18Nable, Serializable
{

   private static final long serialVersionUID = 1L;

   private String id;

   private String lang1id;
   private String lang2id;
   private String lang3id;
   private String lang4id;
   private String lang5id;

   // transient to expose current language and filter researches
   private int lang = 0;

   // ...........................................

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getLang1id()
   {
      return lang1id;
   }

   public void setLang1id(String lang1id)
   {
      this.lang1id = lang1id;
   }

   public String getLang2id()
   {
      return lang2id;
   }

   public void setLang2id(String lang2id)
   {
      this.lang2id = lang2id;
   }

   public String getLang3id()
   {
      return lang3id;
   }

   public void setLang3id(String lang3id)
   {
      this.lang3id = lang3id;
   }

   public String getLang4id()
   {
      return lang4id;
   }

   public void setLang4id(String lang4id)
   {
      this.lang4id = lang4id;
   }

   public String getLang5id()
   {
      return lang5id;
   }

   public void setLang5id(String lang5id)
   {
      this.lang5id = lang5id;
   }

   @Transient
   public int getLang()
   {
      if (id != null && lang == 0)
      {
         lang = id.equals(lang1id) ? 1 : id.equals(lang2id) ? 2 : id
                  .equals(lang3id) ? 3 : id.equals(lang4id) ? 4 : id
                  .equals(lang5id) ? 5 : 0;
      }
      return lang;
   }

   public void setLang(int lang)
   {
      this.lang = lang;
   }

   public void setLangAsString(String lang)
   {
      setLang(Integer.parseInt(lang));
   }

   @Transient
   public String getI18N(int lang)
   {
      switch (lang)
      {
      case 1:
         return lang1id;
      case 2:
         return lang2id;
      case 3:
         return lang3id;
      case 4:
         return lang4id;
      case 5:
         return lang5id;
      default:
         return null;
      }
   }

}
