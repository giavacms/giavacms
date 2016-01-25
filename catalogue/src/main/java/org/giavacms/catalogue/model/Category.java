package org.giavacms.catalogue.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.giavacms.api.util.IdUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = Category.TABLE_NAME)
@XmlRootElement
public class Category implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Category";
   public static final boolean HAS_DETAILS = true;

   public Category()
   {
      super();
   }

   private List<Product> products;

   private String id;
   private String name;
   private String description;
   boolean active = true;
   private int orderNum;

   private String prop1;
   private String prop2;
   private String prop3;
   private String prop4;
   private String prop5;
   private String prop6;
   private String prop7;
   private String prop8;
   private String prop9;
   private String prop10;

   private String ref1;
   private String ref2;
   private String ref3;
   private String ref4;
   private String ref5;
   private String ref6;
   private String ref7;
   private String ref8;
   private String ref9;
   private String ref10;
   private String language;

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
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

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
   }

   @JsonIgnore
   @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
   @OrderBy("name")
   public List<Product> getProducts()
   {
      if (products == null)
         this.products = new ArrayList<Product>();
      return products;
   }

   public void setProducts(List<Product> products)
   {
      this.products = products;
   }

   public void addProduct(Product product)
   {
      getProducts().add(product);
   }

   public int getOrderNum()
   {
      return orderNum;
   }

   public void setOrderNum(int orderNum)
   {
      this.orderNum = orderNum;
   }

   public String getProp1()
   {
      return prop1;
   }

   public void setProp1(String prop1)
   {
      this.prop1 = prop1;
   }

   public String getProp2()
   {
      return prop2;
   }

   public void setProp2(String prop2)
   {
      this.prop2 = prop2;
   }

   public String getProp3()
   {
      return prop3;
   }

   public void setProp3(String prop3)
   {
      this.prop3 = prop3;
   }

   public String getProp4()
   {
      return prop4;
   }

   public void setProp4(String prop4)
   {
      this.prop4 = prop4;
   }

   public String getProp5()
   {
      return prop5;
   }

   public void setProp5(String prop5)
   {
      this.prop5 = prop5;
   }

   public String getProp6()
   {
      return prop6;
   }

   public void setProp6(String prop6)
   {
      this.prop6 = prop6;
   }

   public String getProp7()
   {
      return prop7;
   }

   public void setProp7(String prop7)
   {
      this.prop7 = prop7;
   }

   public String getProp8()
   {
      return prop8;
   }

   public void setProp8(String prop8)
   {
      this.prop8 = prop8;
   }

   public String getProp9()
   {
      return prop9;
   }

   public void setProp9(String prop9)
   {
      this.prop9 = prop9;
   }

   public String getProp10()
   {
      return prop10;
   }

   public void setProp10(String prop10)
   {
      this.prop10 = prop10;
   }

   public String getRef1()
   {
      return ref1;
   }

   public void setRef1(String ref1)
   {
      this.ref1 = ref1;
   }

   public String getRef2()
   {
      return ref2;
   }

   public void setRef2(String ref2)
   {
      this.ref2 = ref2;
   }

   public String getRef3()
   {
      return ref3;
   }

   public void setRef3(String ref3)
   {
      this.ref3 = ref3;
   }

   public String getRef4()
   {
      return ref4;
   }

   public void setRef4(String ref4)
   {
      this.ref4 = ref4;
   }

   public String getRef5()
   {
      return ref5;
   }

   public void setRef5(String ref5)
   {
      this.ref5 = ref5;
   }

   public String getRef6()
   {
      return ref6;
   }

   public void setRef6(String ref6)
   {
      this.ref6 = ref6;
   }

   public String getRef7()
   {
      return ref7;
   }

   public void setRef7(String ref7)
   {
      this.ref7 = ref7;
   }

   public String getRef8()
   {
      return ref8;
   }

   public void setRef8(String ref8)
   {
      this.ref8 = ref8;
   }

   public String getRef9()
   {
      return ref9;
   }

   public void setRef9(String ref9)
   {
      this.ref9 = ref9;
   }

   public String getRef10()
   {
      return ref10;
   }

   public void setRef10(String ref10)
   {
      this.ref10 = ref10;
   }

   @Transient
   @JsonIgnore
   public String getProp(int index)
   {
      switch (index)
      {
      case 1:
         return prop1;
      case 2:
         return prop2;
      case 3:
         return prop3;
      case 4:
         return prop4;
      case 5:
         return prop5;
      case 6:
         return prop6;
      case 7:
         return prop7;
      case 8:
         return prop8;
      case 9:
         return prop9;
      case 10:
         return prop10;
      default:
         return null;
      }
   }

   @Transient
   @JsonIgnore
   public String getRef(int index)
   {
      switch (index)
      {
      case 1:
         return ref1;
      case 2:
         return ref2;
      case 3:
         return ref3;
      case 4:
         return ref4;
      case 5:
         return ref5;
      case 6:
         return ref6;
      case 7:
         return ref7;
      case 8:
         return ref8;
      case 9:
         return ref9;
      case 10:
         return ref10;
      default:
         return null;
      }
   }

   @Transient
   @JsonIgnore
   public int getPropIndex(String prop)
   {
      if (prop == null)
      {
         return 0;
      }
      for (int i = 1; i <= 10; i++)
      {
         if (prop.equals(getProp(i)))
         {
            return i;
         }
      }
      return -1;
   }

   @Transient
   @JsonIgnore
   public int getRefIndex(String ref)
   {
      if (ref == null)
      {
         return 0;
      }
      for (int i = 1; i <= 10; i++)
      {
         if (ref.equals(getRef(i)))
         {
            return i;
         }
      }
      return -1;
   }

   public void setProp(int index, String prop)
   {
      switch (index)
      {
      case 1:
         prop1 = prop;
         return;
      case 2:
         prop2 = prop;
         return;
      case 3:
         prop3 = prop;
         return;
      case 4:
         prop4 = prop;
         return;
      case 5:
         prop5 = prop;
         return;
      case 6:
         prop6 = prop;
         return;
      case 7:
         prop7 = prop;
         return;
      case 8:
         prop8 = prop;
         return;
      case 9:
         prop9 = prop;
         return;
      case 10:
         prop10 = prop;
         return;
      default:
         return;
      }
   }

   public void setRef(int index, String ref)
   {
      switch (index)
      {
      case 1:
         ref1 = ref;
         return;
      case 2:
         ref2 = ref;
         return;
      case 3:
         ref3 = ref;
         return;
      case 4:
         ref4 = ref;
         return;
      case 5:
         ref5 = ref;
         return;
      case 6:
         ref6 = ref;
         return;
      case 7:
         ref7 = ref;
         return;
      case 8:
         ref8 = ref;
         return;
      case 9:
         ref9 = ref;
         return;
      case 10:
         ref10 = ref;
         return;
      default:
         return;
      }
   }

   @Transient
   @JsonIgnore
   public List<String> getProps()
   {
      List<String> props = new ArrayList<String>();
      for (int p = 1; p <= 10; p++)
      {
         props.add(getProp(p));
      }
      return props;
   }

   @Transient
   @JsonIgnore
   public List<String> getRefs()
   {
      List<String> refs = new ArrayList<String>();
      for (int r = 1; r <= 10; r++)
      {
         refs.add(getRef(r));
      }
      return refs;
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
      return "Category [id=" + getId() + ", name=" + getName()
               + ", description=" + getDescription() + ", active="
               + isActive() + "]";
   }

   // private String tmpId;
   // public String getTmpId()
   // {
   // if (tmpId == null)
   // {
   // tmpId = IdUtils.createPageId(name);
   // }
   // return tmpId;
   // }
   //
   // public void setTmpId(String tmpId)
   // {
   // this.tmpId = tmpId;
   // }

}