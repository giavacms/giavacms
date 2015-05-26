package org.giavacms.catalogue.model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiorenzo on 26/05/15.
 */
public class FeatureItems implements Serializable
{
   private static final long serialVersionUID = 1L;

   private String name;
   private List<String> values;

   public FeatureItems()
   {
   }

   public FeatureItems(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<String> getValues()
   {
      if (values == null)
         this.values = new ArrayList<>();
      return values;
   }

   public void setValues(List<String> values)
   {
      this.values = values;
   }

   public void addValue(String value)
   {
      getValues().add(value);
   }
}
