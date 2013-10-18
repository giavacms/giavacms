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

@Entity
public class PageConfiguration implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;

   private boolean withCache;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public boolean isWithCache()
   {
      return withCache;
   }

   public void setWithCache(boolean withCache)
   {
      this.withCache = withCache;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

}