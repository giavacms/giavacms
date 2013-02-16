/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

@Entity
public class MenuGroup implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   boolean active = true;

   // ------------------------------------------------------------------------

   private String name;
   private String description;
   private String path;
   private Integer sortOrder = 1;

   private List<MenuItem> list;
   private List<MenuItem> activeList;

   // ------------------------------------------------------------------------

   boolean dynamic = false;

   // ------------------------------------------------------------------------

   public MenuGroup()
   {
   }

   // ------------------------------------------------------------------------

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @OrderBy("sortOrder, name")
   public List<MenuItem> getList()
   {
      if (list == null)
         this.list = new ArrayList<MenuItem>();
      return list;
   }

   public void setList(List<MenuItem> list)
   {
      this.list = list;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public String getPath()
   {
      return path;
   }

   public void setPath(String path)
   {
      this.path = path;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public Integer getSortOrder()
   {
      return sortOrder;
   }

   public void setSortOrder(Integer sortOrder)
   {
      this.sortOrder = sortOrder;
   }

   @Transient
   public int getListSize()
   {
      return this.list == null ? 0 : list.size();
   }

   @Transient
   public List<MenuItem> getActiveList()
   {
      if (activeList == null)
         activeList = new ArrayList<MenuItem>();
      return activeList;
   }

   public void setActiveList(List<MenuItem> activeList)
   {
      this.activeList = activeList;
   }

   @Transient
   public int getActiveListSize()
   {
      return this.activeList == null ? 0 : activeList.size();
   }

   public boolean isDynamic()
   {
      return dynamic;
   }

   public void setDynamic(boolean dynamic)
   {
      this.dynamic = dynamic;
   }

   @Transient
   public MenuItem getDynamicItem()
   {
      if (!dynamic || getActiveList().size() == 0)
      {
         return null;
      }
      return getActiveList().get(0);
   }

   @Override
   public String toString()
   {
      return "MenuGroup [id=" + id + ", active=" + active + ", name=" + name
               + ", description=" + description + ", path=" + path
               + ", sortOrder=" + sortOrder + ", dynamic=" + dynamic + "]";
   }

}
