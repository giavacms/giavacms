/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.service.rs.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.giavacms.catalogue.model.Category;

@XmlRootElement
public class ProductRs implements Serializable
{

   private static final long serialVersionUID = 1L;
   private String preview;
   private Category category;
   private String dimensions;
   private String code;
   private String price;
   private String vat;

   private String val1;
   private String val2;
   private String val3;
   private String val4;
   private String val5;
   private String val6;
   private String val7;
   private String val8;
   private String val9;
   private String val10;

   public String getPreview()
   {
      return preview;
   }

   public void setPreview(String preview)
   {
      this.preview = preview;
   }

   public Category getCategory()
   {
      return category;
   }

   public void setCategory(Category category)
   {
      this.category = category;
   }

   public String getDimensions()
   {
      return dimensions;
   }

   public void setDimensions(String dimensions)
   {
      this.dimensions = dimensions;
   }

   public String getCode()
   {
      return code;
   }

   public void setCode(String code)
   {
      this.code = code;
   }

   public String getPrice()
   {
      return price;
   }

   public void setPrice(String price)
   {
      this.price = price;
   }

   public String getVat()
   {
      return vat;
   }

   public void setVat(String vat)
   {
      this.vat = vat;
   }

   public String getVal1()
   {
      return val1;
   }

   public void setVal1(String val1)
   {
      this.val1 = val1;
   }

   public String getVal2()
   {
      return val2;
   }

   public void setVal2(String val2)
   {
      this.val2 = val2;
   }

   public String getVal3()
   {
      return val3;
   }

   public void setVal3(String val3)
   {
      this.val3 = val3;
   }

   public String getVal4()
   {
      return val4;
   }

   public void setVal4(String val4)
   {
      this.val4 = val4;
   }

   public String getVal5()
   {
      return val5;
   }

   public void setVal5(String val5)
   {
      this.val5 = val5;
   }

   public String getVal6()
   {
      return val6;
   }

   public void setVal6(String val6)
   {
      this.val6 = val6;
   }

   public String getVal7()
   {
      return val7;
   }

   public void setVal7(String val7)
   {
      this.val7 = val7;
   }

   public String getVal8()
   {
      return val8;
   }

   public void setVal8(String val8)
   {
      this.val8 = val8;
   }

   public String getVal9()
   {
      return val9;
   }

   public void setVal9(String val9)
   {
      this.val9 = val9;
   }

   public String getVal10()
   {
      return val10;
   }

   public void setVal10(String val10)
   {
      this.val10 = val10;
   }
}
