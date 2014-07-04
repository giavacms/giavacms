/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.event.LanguageEvent;
import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractController;

@Named
@SessionScoped
public class I18nController extends AbstractController<Page>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @Inject
   Event<LanguageEvent> languageEvent;

   int languageToEdit = 0;

   public int getLanguageToEdit()
   {
      return languageToEdit;
   }

   public void setLanguageToEdit(int languageToEdit)
   {
      this.languageToEdit = languageToEdit;
   }

   @Inject
   @OwnRepository(PageRepository.class)
   PageRepository pageRepository;

   @Override
   public Object getId(Page t)
   {
      return t.getId();
   }

   public void deassociate(String formerAlternatePageId, boolean bidirectional)
   {
      try
      {
         switch (languageToEdit)
         {
         case 1:
            getElement().setLang1id(null);
            break;
         case 2:
            getElement().setLang2id(null);
            break;
         case 3:
            getElement().setLang3id(null);
            break;
         case 4:
            getElement().setLang4id(null);
            break;
         case 5:
            getElement().setLang5id(null);
            break;
         default:
            break;
         }

         pageRepository.resetLanguage(languageToEdit, getElement().getId());

         if (getElement().getId().equals(formerAlternatePageId))
         {
            return;
         }

         if (!bidirectional)
         {
            return;
         }

         List<Integer> currentLanguages = new ArrayList<Integer>();
         if (getElement().getId().equals(getElement().getLang1id()))
         {
            currentLanguages.add(1);
         }
         if (getElement().getId().equals(getElement().getLang2id()))
         {
            currentLanguages.add(2);
         }
         if (getElement().getId().equals(getElement().getLang3id()))
         {
            currentLanguages.add(3);
         }
         if (getElement().getId().equals(getElement().getLang4id()))
         {
            currentLanguages.add(4);
         }
         if (getElement().getId().equals(getElement().getLang5id()))
         {
            currentLanguages.add(5);
         }

         if (currentLanguages.size() > 1)
         {
            logger.warn("Bidirectional language setup is only implemented in case of one language!");
            super.addFacesMessage("Attenzione: il setup bidirezionale e' possibile solo in caso la pagina corrente sia associata a una sola lingua");
            return;
         }
         pageRepository.resetLanguage(currentLanguages.get(0), formerAlternatePageId);
      }
      catch (Exception e)
      {
         logger.warn(e.getMessage(), e);
         super.addFacesMessage("Errori nella lavorazione");
      }
      finally
      {
         setModel(null);
      }

   }

   public void associate(String newAlternatePageId, boolean bidirectional)
   {
      try
      {
         String formerAlternatePageId = null;
         switch (languageToEdit)
         {
         case 1:
            formerAlternatePageId = getElement().getLang1id();
            getElement().setLang1id(newAlternatePageId);
            break;
         case 2:
            formerAlternatePageId = getElement().getLang2id();
            getElement().setLang2id(newAlternatePageId);
            break;
         case 3:
            formerAlternatePageId = getElement().getLang3id();
            getElement().setLang3id(newAlternatePageId);
            break;
         case 4:
            formerAlternatePageId = getElement().getLang4id();
            getElement().setLang4id(newAlternatePageId);
            break;
         case 5:
            formerAlternatePageId = getElement().getLang5id();
            getElement().setLang5id(newAlternatePageId);
            break;
         default:
            break;
         }

         pageRepository.setupLanguage(languageToEdit, getElement().getId(), newAlternatePageId);
         pageRepository.setupLanguage(languageToEdit, newAlternatePageId, newAlternatePageId);
         if (!bidirectional)
         {
            return;
         }

         List<Integer> currentLanguages = new ArrayList<Integer>();
         if (getElement().getId().equals(getElement().getLang1id()))
         {
            currentLanguages.add(1);
         }
         if (getElement().getId().equals(getElement().getLang2id()))
         {
            currentLanguages.add(2);
         }
         if (getElement().getId().equals(getElement().getLang3id()))
         {
            currentLanguages.add(3);
         }
         if (getElement().getId().equals(getElement().getLang4id()))
         {
            currentLanguages.add(4);
         }
         if (getElement().getId().equals(getElement().getLang5id()))
         {
            currentLanguages.add(5);
         }

         if (currentLanguages.size() > 1)
         {
            logger.warn("Bidirectional language setup is only implemented in case of one language!");
            super.addFacesMessage("Attenzione: il setup bidirezionale e' possibile solo in caso la pagina corrente sia associata a una sola lingua");
            return;
         }

         pageRepository.setupLanguage(currentLanguages.get(0), newAlternatePageId, getElement().getId());
         if (formerAlternatePageId != null)
         {
            pageRepository.resetLanguage(currentLanguages.get(0), formerAlternatePageId);
         }

      }
      catch (Exception e)
      {
         logger.warn(e.getMessage(), e);
         super.addFacesMessage("Errori nella lavorazione");
      }
      finally
      {
         setModel(null);
      }
   }

   @Override
   protected void preReload()
   {
      setModel(null);
      setLoaded(true);
   }

   public void modElement(String id)
   {
      this.reset();
      Page t = getRepository().fetch(id);
      setElement(t);
      setEditMode(true);
      setReadOnlyMode(false);
      if (t.getExtension() == null)
      {
         getSearch().getObj().setExtended(false);
         getSearch().getObj().setExtension(null);
         getSearch().getObj().setClone(false);
      }
      else
      {
         getSearch().getObj().setExtended(true);
         getSearch().getObj().setExtension(t.getExtension());
         getSearch().getObj().setClone(true);
      }
   }
}