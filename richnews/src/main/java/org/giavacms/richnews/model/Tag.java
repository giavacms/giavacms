package org.giavacms.richnews.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tag implements Serializable
{

   private static final long serialVersionUID = 1L;
   private String id;
   private int occurrences;

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public int getOccurrences()
   {
      return occurrences;
   }

   public void setOccurrences(int occurrences)
   {
      this.occurrences = occurrences;
   }

}
