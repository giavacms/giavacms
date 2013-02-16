/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.MenuGroup;
import org.giavacms.base.model.MenuItem;
import org.giavacms.base.model.Page;
import org.giavacms.base.pojo.PageDataModel;
import org.giavacms.base.producer.BaseProducer;
import org.giavacms.base.repository.MenuRepository;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;


@Named
@SessionScoped
public class MenuController extends AbstractLazyController<MenuGroup>
{

   private static final long serialVersionUID = 1L;

   public static final String BASE_PATH = "/pagine/";

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/menu/view.xhtml";
   @ListPage
   public static String LIST = "/private/menu/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/menu/edit1.xhtml";
   public static String NEW_OR_EDIT2_DYNAMIC = "/private/menu/edit2d.xhtml";
   public static String NEW_OR_EDIT2_STATIC = "/private/menu/edit2s.xhtml";
   public static String NEW_OR_EDIT3 = "/private/menu/edit3.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(MenuRepository.class)
   MenuRepository menuRepository;

   @Inject
   PageRepository pageRepository;

   @Inject
   BaseProducer baseProducer;

   @Inject
   MenuHolder menuHolder;

   private DataModel<MenuItem> menuItemModel;
   private PageDataModel pages;
   private Page[] selectedPages;

   // ------------------------------------------------

   /**
    * Obbligatoria l'invocazione 'appropriata' di questo super construttore protetto da parte delle sottoclassi
    */
   public MenuController()
   {

   }

   // -----------------------------------------------------

   @Override
   public String reset()
   {
      menuHolder.reset();
      return super.reset();
   }

   @Override
   public String addElement()
   {
      try
      {
         setElement(new MenuGroup());
         getElement().setPath(BASE_PATH);
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
      }
      return editPage();
   }

   // -----------------------------------------------------
   @Override
   public String save()
   {
      super.save();
      return viewPage();
   }

   public String update()
   {
      super.update();
      return viewPage();
   }

   @Override
   public String delete()
   {
      super.delete();
      return listPage();
   }

   // ----------------------------------------------------
   // ----------------------------------------------------
   // ----------------------------------------------------

   public String step2()
   {
      this.pages = new PageDataModel(pageRepository.getAllList());
      List<Page> selectedPages = new ArrayList<Page>();
      for (Page page : pages)
      {
         for (MenuItem menuItem : getElement().getList())
         {
            if (!menuItem.isActive())
            {
               continue;
            }
            if (menuItem.getPage().getId().equals(page.getId()))
            {
               selectedPages.add(page);
            }
         }
      }
      if (!getElement().isDynamic())
      {
         this.selectedPages = selectedPages.toArray(new Page[] {});
         return NEW_OR_EDIT2_STATIC;
      }
      else
      {
         for (MenuItem m : getElement().getList())
         {
            m.setActive(false);
         }
         if (getElement().getActiveList().size() == 0)
         {
            MenuItem dynamicItem = new MenuItem();
            dynamicItem.setGroup(getElement());
            dynamicItem.setPage(new Page());
            getElement().getList().add(0, dynamicItem);
            getElement().getActiveList().add(0, dynamicItem);
         }
         return NEW_OR_EDIT2_DYNAMIC;
      }
   }

   public Page[] getSelectedPages()
   {
      return selectedPages;
   }

   public void setSelectedPages(Page[] selectedPages)
   {
      this.selectedPages = selectedPages;
   }

   public void updateSelectedPages()
   {
      // evito doppioni, almeno... beccare le deselezioni ancora non ho capito
      // come..
      Map<String, Page> unique_selected_pages = new HashMap<String, Page>();
      if (selectedPages != null)
      {
         for (Page p : selectedPages)
         {
            unique_selected_pages.put(p.getId(), p);
         }
      }
      selectedPages = unique_selected_pages.values().toArray(new Page[] {});
   }

   public PageDataModel getPages()
   {
      return pages;
   }

   public void setPages(PageDataModel pages)
   {
      this.pages = pages;
   }

   public String step3s()
   {
      List<Page> pagesToAdd = new ArrayList<Page>();
      for (Page selectedPage : this.selectedPages)
      {
         boolean already_present = false;
         for (MenuItem menuItem : getElement().getList())
         {
            if (!menuItem.isActive())
            {
               continue;
            }
            if (menuItem.getPage().getId().equals(selectedPage.getId()))
            {
               already_present = true;
            }
         }
         if (!already_present)
         {
            pagesToAdd.add(selectedPage);
         }
      }
      List<MenuItem> menuItemsToRemove = new ArrayList<MenuItem>();
      for (MenuItem menuItem : getElement().getList())
      {
         if (!menuItem.isActive())
         {
            continue;
         }
         boolean kept = false;
         for (Page selectedPage : this.selectedPages)
         {
            if (selectedPage.getId().equals(menuItem.getPage().getId()))
            {
               kept = true;
            }
         }
         if (!kept)
         {
            menuItemsToRemove.add(menuItem);
         }
      }

      for (Page pageToAdd : pagesToAdd)
      {
         MenuItem menuItem = new MenuItem();
         menuItem.setName(pageToAdd.getTitle());
         String percorso = getElement().getPath()
                  + (getElement().getPath().endsWith("/") ? "" : "/")
                  + pageToAdd.getId();
         menuItem.setPath(percorso);
         menuItem.setDescription(pageToAdd.getDescription());
         menuItem.setPage(pageToAdd);
         menuItem.setGroup(getElement());
         getElement().getList().add(menuItem);
      }
      for (MenuItem menuItemToRemove : menuItemsToRemove)
      {
         menuItemToRemove.setActive(false);
      }
      int i = 0;
      List<MenuItem> menuItem2dataModel = new ArrayList<MenuItem>();
      for (MenuItem menuItem : getElement().getList())
      {
         if (menuItem.isActive())
         {
            menuItem.setSortOrder(++i);
            menuItem2dataModel.add(menuItem);
         }
      }
      this.menuItemModel = new ListDataModel<MenuItem>(menuItem2dataModel);
      return NEW_OR_EDIT3;
   }

   public String removeItem(Long id)
   {
      int i = 0;
      for (MenuItem menuItem : getElement().getList())
      {
         if (menuItem.getId() != null && menuItem.getId().equals(id))
         {
            menuItem.setActive(false);
         }
         else
         {
            menuItem.setSortOrder(++i);
         }
      }
      List<MenuItem> menuItem2dataModel = new ArrayList<MenuItem>();
      for (MenuItem menuItem : getElement().getList())
      {
         if (menuItem.isActive())
         {
            menuItem2dataModel.add(menuItem);
         }
      }
      this.menuItemModel = new ListDataModel<MenuItem>(menuItem2dataModel);
      return NEW_OR_EDIT3;
   }

   public DataModel<MenuItem> getMenuItemModel()
   {
      return menuItemModel;
   }

   public void setMenuItemModel(DataModel<MenuItem> menuItemModel)
   {
      this.menuItemModel = menuItemModel;
   }

   public String step3d()
   {
      getElement().getDynamicItem().setPage(
               pageRepository.find(getElement().getDynamicItem().getPage()
                        .getId()));
      getElement().getDynamicItem().setDescription(
               getElement().getDynamicItem().getPage().getDescription());
      getElement().getDynamicItem().setPath(
               BASE_PATH + getElement().getDynamicItem().getPage().getId()
                        + "?filter=");
      getElement().getDynamicItem().setSortOrder(1);
      List<MenuItem> menuItem2dataModel = new ArrayList<MenuItem>();
      menuItem2dataModel.add(getElement().getDynamicItem());
      this.menuItemModel = new ListDataModel<MenuItem>(menuItem2dataModel);
      return NEW_OR_EDIT3;
   }

}