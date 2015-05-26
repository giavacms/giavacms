/*
 * Copyright 213 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = Feature.TABLE_NAME)
public class Feature implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Feature";

   public Feature()
   {
   }

   private Long id;
   private boolean active = true;
   private String name;
   private String option;

   @Id
   @GeneratedValue
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

   @Column(name = "anOption")
   public String getOption()
   {
      return option;
   }

   public void setOption(String option)
   {
      this.option = option;
   }

   @JsonIgnore
   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

}
