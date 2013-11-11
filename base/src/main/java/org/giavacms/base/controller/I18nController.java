/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractController;

@Named
@SessionScoped
public class I18nController extends AbstractController<Page>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static final String BACK = "/private/administration.xhtml";
   @ListPage
   @ViewPage
   @EditPage
   public static final String LIST = "/private/i18n/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(PageRepository.class)
   PageRepository pageRepository;

   @Override
   public Object getId(Page t)
   {
      return t.getId();
   }

   @Override
   public String update()
   {
      String outcome = super.update();
      setElement(null);
      return outcome;
   }

   public void searchLanguage(Long l)
   {
      getSearch().getObj().setLang(l.intValue());
      refreshModel();
   }

   public void resetLanguage(Long l)
   {
      setLanguage(l, null);
      pageRepository.resetLanguage(l,getElement().getId());
   }

   public void setLanguage(Long l, String pageId)
   {
      Page alternate = null;
      switch (l.intValue())
      {
      case 1:
         if (getElement().getLang1id() != null)
         {
            alternate = getRepository().fetch(getElement().getLang1id());
         }
         getElement().setLang1id(pageId);
         break;
      case 2:
         if (getElement().getLang2id() != null)
         {
            alternate = getRepository().fetch(getElement().getLang2id());
         }
         getElement().setLang2id(pageId);
         break;
      case 3:
         if (getElement().getLang3id() != null)
         {
            alternate = getRepository().fetch(getElement().getLang3id());
         }
         getElement().setLang3id(pageId);
         break;
      case 4:
         if (getElement().getLang4id() != null)
         {
            alternate = getRepository().fetch(getElement().getLang4id());
         }
         getElement().setLang4id(pageId);
         break;
      case 5:
         if (getElement().getLang5id() != null)
         {
            alternate = getRepository().fetch(getElement().getLang5id());
         }
         getElement().setLang5id(pageId);
         break;
      default:
         break;
      }
      getRepository().update(getElement());
      if (alternate == null && pageId != null
               && !getElement().getId().equals(pageId))
      {
         alternate = getRepository().fetch(pageId);
      }
      if (alternate != null)
      {
         setReverseLanguage(alternate, l.intValue(), pageId == null ? null
                  : getElement().getId());
      }
   }

   private void setReverseLanguage(Page alternate, int alternateLang,
            String pageId)
   {
      switch (getElement().getLang())
      {
      case 1:
         alternate.setLang1id(pageId);
         break;
      case 2:
         alternate.setLang2id(pageId);
         break;
      case 3:
         alternate.setLang3id(pageId);
         break;
      case 4:
         alternate.setLang4id(pageId);
         break;
      case 5:
         alternate.setLang5id(pageId);
         break;
      default:
         break;
      }
      switch (alternateLang)
      {
      case 1:
         alternate.setLang1id(alternate.getId());
         break;
      case 2:
         alternate.setLang2id(alternate.getId());
         break;
      case 3:
         alternate.setLang3id(alternate.getId());
         break;
      case 4:
         alternate.setLang4id(alternate.getId());
         break;
      case 5:
         alternate.setLang5id(alternate.getId());
         break;
      default:
         break;
      }
      getRepository().update(alternate);
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
      else {
         getSearch().getObj().setExtended(true);
         getSearch().getObj().setExtension(t.getExtension());
         getSearch().getObj().setClone(true);
      }
   }
}