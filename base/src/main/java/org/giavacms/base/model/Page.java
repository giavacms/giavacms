/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Page implements I18Nable, Serializable
{

   private static final long serialVersionUID = 1L;

   // ------------------------------------------------------------------------

   private String id;
   boolean active = true;
   private String title;
   private String description;
   private TemplateImpl template;

   private String lang1id;
   private String lang2id;
   private String lang3id;
   private String lang4id;
   private String lang5id;

   // transient to expose current language and filter researches
   private int lang = 0;

   // ------------------------------------------------------------------------

   // transiente, per accumulare il risultato finale
   private String content;

   // ------------------------------------------------------------------------

   public Page()
   {
   }

   // ------------------------------------------------------------------------

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   public TemplateImpl getTemplate()
   {
      if (template == null)
         this.template = new TemplateImpl();
      return template;
   }

   public void setTemplate(TemplateImpl template)
   {
      this.template = template;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   @Lob
   @Column(length = 1024)
   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   // ------------------------------------------------------------------------

   @Transient
   public String getContent()
   {
      return content;
   }

   public void setContent(String content)
   {
      this.content = content;
   }

   // ------------------------------------------------------------------------

   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof Page))
         return false;
      Page p = (Page) o;
      return p.getId() == null ? false : p.getId().equals(this.id);
   }

   @Override
   public String toString()
   {
      return "Page [id=" + id + ", active=" + active + ", title=" + title
               + ", description=" + description + ", template="
               + template.getTemplate().getName() + ", content=" + content
               + "]";
   }

   @Override
   public int hashCode()
   {
      return (this.id != null) ? this.id.hashCode() : super.hashCode();
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
