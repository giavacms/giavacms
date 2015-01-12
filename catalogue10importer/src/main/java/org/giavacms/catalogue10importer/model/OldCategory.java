package org.giavacms.catalogue10importer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "Category_Old")
public class OldCategory
         implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String name;
   private String description;
   private List<OldProduct> products;
   private boolean active = true;
   private int orderNum;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @Lob
   @Column(length = 1024)
   public String getDescription()
   {
      return this.description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
   @OrderBy("name")
   public List<OldProduct> getProducts()
   {
      if (this.products == null)
         this.products = new ArrayList<OldProduct>();
      return this.products;
   }

   public void setProducts(List<OldProduct> products)
   {
      this.products = products;
   }

   public void addProduct(OldProduct product)
   {
      getProducts().add(product);
   }

   public String toString()
   {
      return "Category [id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", active="
               + this.active + "]";
   }

   public boolean isActive()
   {
      return this.active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public int getOrderNum()
   {
      return this.orderNum;
   }

   public void setOrderNum(int orderNum)
   {
      this.orderNum = orderNum;
   }
}