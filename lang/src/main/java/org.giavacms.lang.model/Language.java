package org.giavacms.lang.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Language implements Serializable
{
   private String name;

   public Language()
   {
   }

   public Language(String name)
   {
      this.name = name;
   }

   @Id
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
}
