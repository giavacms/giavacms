package org.giavacms.richnews10importer.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RichNewsType implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private boolean active = true;
   private String name;

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

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Override
   public String toString()
   {
      return "RichNewsType [id=" + id + ", active=" + active + ", name="
               + name + "]";
   }

}
