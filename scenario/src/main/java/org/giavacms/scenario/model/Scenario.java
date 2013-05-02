package org.giavacms.scenario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;

@Entity
@DiscriminatorValue(value = Scenario.EXTENSION)
public class Scenario extends Page
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "Scenario";

   private String preview;
   private List<Product> products;
   private List<Document> documents;
   private List<Image> images;

   public Scenario()
   {
      super();
      super.setExtension(EXTENSION);
   }

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
   public String getPreview()
   {
      return preview;
   }

   public void setPreview(String preview)
   {
      this.preview = preview;
   }

   @ManyToMany(fetch = FetchType.EAGER)
   public List<Product> getProducts()
   {
      if (products == null)
         this.products = new ArrayList<Product>();
      return products;
   }

   @Transient
   public String getProductNames()
   {
      StringBuffer productNames = new StringBuffer();
      for (Product product : getProducts())
      {
         productNames.append("," + product.getTitle());
      }
      return productNames.length() > 0 ? productNames.toString().substring(1)
               : "";
   }

   public void setProducts(List<Product> products)
   {
      this.products = products;
   }

   public void addProduct(Product product)
   {
      getProducts().add(product);
   }

   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "Scenario_Document", joinColumns = @JoinColumn(name = "Scenario_id"), inverseJoinColumns = @JoinColumn(name = "documents_id"))
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
   @JoinTable(name = "Scenario_Image", joinColumns = @JoinColumn(name = "Scenario_id"), inverseJoinColumns = @JoinColumn(name = "images_id"))
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

   @Override
   public String toString()
   {
      return "Scenario [preview=" + preview + ", products=" + products + ", documents=" + documents + ", images="
               + images + "]";
   }

}
