/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class MenuItem implements Serializable
{

   private static final long serialVersionUID = 1L;

   // ------------------------------------------------------------------------

   private Long id;
   boolean active = true;

   private String name;
   private String description;
   private String path;
   private Integer sortOrder = 1;

   private Page page;
   private MenuGroup group;
   boolean absolute = false;

   private Long version;

   // ------------------------------------------------------------------------

   public MenuItem()
   {
   }

   public MenuItem(Page page, MenuGroup group)
   {
      this.page = page;
      this.name = page.getTitle();
      this.group = group;
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

   @ManyToOne
   public Page getPage()
   {
      return page;
   }

   public void setPage(Page page)
   {
      this.page = page;
   }

   @ManyToOne
   public MenuGroup getGroup()
   {
      return group;
   }

   public void setGroup(MenuGroup group)
   {
      this.group = group;
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

   @Version
   public Long getVersion()
   {
      return version;
   }

   public void setVersion(Long version)
   {
      this.version = version;
   }

   public boolean isAbsolute()
   {
      return absolute;
   }

   public void setAbsolute(boolean absolute)
   {
      this.absolute = absolute;
   }

   @Override
   public String toString()
   {
      return "MenuItem [id=" + id + ", active=" + active + ", name=" + name
               + ", description=" + description + ", path=" + path
               + ", sortOrder=" + sortOrder + ", page=" + page + ", group="
               + group + ", absolute=" + absolute + ", version=" + version
               + "]";
   }

}
