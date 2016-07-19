package org.giavacms.mvel.model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.giavacms.mvel.model.enums.MvelPlaceHolderType;

public class MvelPlaceholder implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String name;

   private List<String> values;

   private MvelPlaceHolderType type = MvelPlaceHolderType.SINGLE;

   public String getName()
   {
      return this.name;
   }

   public void setName(String nome)
   {
      this.name = nome;
   }

   public List<String> getValues()
   {
      if (this.values == null)
      {
         this.values = new ArrayList<>();
      }
      return this.values;
   }

   public void setValues(List<String> valori)
   {
      this.values = valori;
   }

   public MvelPlaceHolderType getType()
   {
      return this.type;
   }

   public void setType(MvelPlaceHolderType type)
   {
      this.type = type;
   }

   @Override
   public String toString()
   {
      return "MvelPlaceholder [name=" + name + ", values=" + values + ", type=" + type + "]";
   }

}
