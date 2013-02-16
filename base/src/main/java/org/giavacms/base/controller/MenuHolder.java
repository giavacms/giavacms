/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.MenuGroup;
import org.giavacms.base.model.MenuItem;
import org.giavacms.base.repository.MenuRepository;
import org.jboss.logging.Logger;


@Named
@ApplicationScoped
public class MenuHolder implements Serializable
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @Inject
   MenuRepository menuRepository;

   // ------------------------------------------------

   protected Logger logger = Logger.getLogger(getClass());

   // ------------------------------------------------

   public MenuHolder()
   {
      // TODO Auto-generated constructor stub
   }

   private List<MenuGroup> list = null;
   private Map<String, MenuGroup> map = null;

   public List<MenuGroup> getGruppi()
   {
      if (list == null)
         init();
      return list;
   }

   public MenuGroup get(String nome)
   {
      if (map == null)
         init();
      MenuGroup g = map.get(nome);
      if (g == null)
      {
         g = new MenuGroup();
         g.setName(nome);
         g.setDescription(nome);
         g.setList(new ArrayList<MenuItem>());
         g.setSortOrder(1);
         g.setPath("");
      }
      return g;
   }

   public void init()
   {
      if (list == null)
      {
         list = menuRepository.getAllList();
         Collections.sort(list, new Comparator<MenuGroup>()
         {

            public int compare(MenuGroup mg1, MenuGroup mg2)
            {
               return mg1.getSortOrder() != null ? (mg2.getSortOrder() != null ? mg1
                        .getSortOrder().compareTo(mg2.getSortOrder())
                        : 1)
                        : (mg2.getSortOrder() != null ? -1 : mg1
                                 .getName().compareTo(mg2.getName()));
            }
         });
         map = new HashMap<String, MenuGroup>();
         for (MenuGroup mg : list)
         {
            map.put(mg.getName(), mg);
         }
      }
   }

   public void reset()
   {
      this.list = null;
      this.map = null;
   }

}