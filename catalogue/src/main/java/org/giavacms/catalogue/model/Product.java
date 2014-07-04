/*
 * Copyright 213 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

@Entity
@DiscriminatorValue(value = Product.EXTENSION)
@Table(name = Product.TABLE_NAME)
public class Product extends Page implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "Product";
   public static final String TABLE_NAME = "Product";
   public static final boolean HAS_DETAILS = true;

   public Product()
   {
      super();
      super.setExtension(EXTENSION);
   }

   // private Long id --> super.id;
   // private String name --> super.title;
   // private String description --> super.description;

   private String preview;
   private Category category;
   private String dimensions;
   private String code;
   private List<Document> documents;
   private List<Image> images;
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

   // private boolean active = true; --> super.active

   @Transient
   @Deprecated
   public String getName()
   {
      return super.getTitle();
   }

   @Deprecated
   public void setName(String name)
   {
      super.setTitle(name);
   }

   @Lob
   @Column(length = 124)
   public String getPreview()
   {
      return preview;
   }

   public void setPreview(String preview)
   {
      this.preview = preview;
   }

   @ManyToOne
   public Category getCategory()
   {
      if (this.category == null)
         this.category = new Category();
      return category;
   }

   public void setCategory(Category category)
   {
      this.category = category;
   }

   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "Product_Document", joinColumns = @JoinColumn(name = "Product_id"), inverseJoinColumns = @JoinColumn(name = "documents_id"))
   public List<Document> getDocuments()
   {
      if (this.documents == null)
         this.documents = new ArrayList<Document>();
      return documents;
   }

   public void setDocuments(List<Document> documents)
   {
      this.documents = documents;
   }

   public void addDocument(Document document)
   {
      getDocuments().add(document);
   }

   @Transient
   public int getDocSize()
   {
      return getDocuments().size();
   }

   @Transient
   public Image getImage()
   {
      if (getImages() != null && getImages().size() > 0)
         return getImages().get(0);
      return null;
   }

   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "Product_Image", joinColumns = @JoinColumn(name = "Product_id"), inverseJoinColumns = @JoinColumn(name = "images_id"))
   public List<Image> getImages()
   {
      if (this.images == null)
         this.images = new ArrayList<Image>();
      return images;
   }

   public void setImages(List<Image> images)
   {
      this.images = images;
   }

   public void addImage(Image image)
   {
      getImages().add(image);
   }

   @Transient
   public int getImgSize()
   {
      return getImages().size();
   }

   public String getCode()
   {
      return code;
   }

   public void setCode(String code)
   {
      this.code = code;
   }

   public String getDimensions()
   {
      return dimensions;
   }

   public void setDimensions(String dimensions)
   {
      this.dimensions = dimensions;
   }

   @Override
   public String toString()
   {
      return "Product [id=" + super.getId() + ", title=" + super.getTitle()
               + ", preview=" + preview + ", description="
               + super.getDescription() + ", category=" + (category == null ? null : category.getTitle())
               + ", dimensions=" + dimensions + ", code=" + code + ", active="
               + super.isActive() + "]";
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

   public String getVal(int index)
   {
      switch (index)
      {
      case 1:
         return val1;
      case 2:
         return val2;
      case 3:
         return val3;
      case 4:
         return val4;
      case 5:
         return val5;
      case 6:
         return val6;
      case 7:
         return val7;
      case 8:
         return val8;
      case 9:
         return val9;
      case 10:
         return val10;
      default:
         return null;
      }
   }

   public void setVal(int index, String val)
   {
      switch (index)
      {
      case 1:
         val1 = val;
         return;
      case 2:
         val2 = val;
         return;
      case 3:
         val3 = val;
         return;
      case 4:
         val4 = val;
         return;
      case 5:
         val5 = val;
         return;
      case 6:
         val6 = val;
         return;
      case 7:
         val7 = val;
         return;
      case 8:
         val8 = val;
         return;
      case 9:
         val9 = val;
         return;
      case 10:
         val10 = val;
         return;
      default:
         return;
      }
   }

   public String getProp(String prop)
   {
      if (prop == null || prop.trim().length() == 0 || category == null)
      {
         return null;
      }
      return getVal(category.getPropIndex(prop));
   }

   public void setProp(String prop, String val)
   {
      if (prop == null || prop.trim().length() == 0 || category == null)
      {
         return;
      }
      setVal(category.getPropIndex(prop), val);
   }

   public String getRef(String ref)
   {
      if (ref == null || ref.trim().length() == 0 || category == null)
      {
         return null;
      }
      return getVal(category.getRefIndex(ref));
   }

   public void setRef(String ref, String val)
   {
      if (ref == null || ref.trim().length() == 0 || category == null)
      {
         return;
      }
      setVal(category.getRefIndex(ref), val);
   }


}
