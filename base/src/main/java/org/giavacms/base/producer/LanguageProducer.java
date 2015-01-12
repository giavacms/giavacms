/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.annotation.Languages;
import org.giavacms.base.model.I18nSupport;
import org.giavacms.base.model.Language;
import org.giavacms.base.repository.LanguageRepository;
import org.jboss.logging.Logger;

@Singleton
public class LanguageProducer implements Serializable
{

   Logger logger = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;

   @Inject
   LanguageRepository languageRepository;

   Language language1 = null;
   Language language2 = null;
   Language language3 = null;
   Language language4 = null;
   Language language5 = null;

   List<Language> languages = null;

   @Produces
   @Named
   public Language getLanguage1()
   {
      return language1;
   }

   @Produces
   @Named
   public Language getLanguage2()
   {
      return language2;
   }

   @Produces
   @Named
   public Language getLanguage3()
   {
      return language3;
   }

   @Produces
   @Named
   public Language getLanguage4()
   {
      return language4;
   }

   @Produces
   @Named
   public Language getLanguage5()
   {
      return language5;
   }

   @PostConstruct
   public void reset()
   {
      this.language1 = null;
      this.language2 = null;
      this.language3 = null;
      this.language4 = null;
      this.language5 = null;
      List<Language> allLanguages = languageRepository.getAllList();
      // maximum size as possible
      languages = Arrays.asList(new Language[allLanguages.size()+1]);
      if (allLanguages != null)
      {
         for (Language language : allLanguages)
         {
            if (language.isEnabled())
            {
               languages.set(language.getPosition(), language);
               if (language.getPosition() == 1)
               {
                  this.language1 = language;
               }
               else if (language.getPosition() == 2)
               {
                  this.language2 = language;
               }
               else if (language.getPosition() == 3)
               {
                  this.language3 = language;
               }
               else if (language.getPosition() == 4)
               {
                  this.language4 = language;
               }
               else if (language.getPosition() == 5)
               {
                  this.language5 = language;
               }
            }
         }
      }
   }

   @Produces
   @Languages
   @Named
   public List<Language> getLanguages()
   {
      return languages;
   }

   @Produces
   @Named
   public SelectItem[] getLanguageItems()
   {
      List<SelectItem> languageItemsList = new ArrayList<SelectItem>();
      languageItemsList.add(new SelectItem(0,"lingua..."));
      for (Language l : languages)
      {
         if (l != null)
         {
            languageItemsList.add(new SelectItem(l.getPosition(), l.getId()));
         }
      }
      return languageItemsList.toArray(new SelectItem[] {});
   }

   public String getLanguage(I18nSupport i18nSupport)
   {
      if (languages == null || languages.size() == 0)
      {
         return null;
      }
      if (i18nSupport.getLang() > 0 && i18nSupport.getLang() <= languages.size())
      {
         try
         {
            return languages.get(i18nSupport.getLang() - 1).getDescription();
         }
         catch (Exception e)
         {
            e.printStackTrace();
            return null;
         }
      }
      return null;
   }

}
