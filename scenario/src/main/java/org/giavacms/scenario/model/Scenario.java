package org.giavacms.scenario.model;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = Scenario.TABLE_NAME)
public class Scenario implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Scenario";
   public static final boolean HAS_DETAILS = true;

   private String name;
   private String preview;
   private List<Product> products;
   private List<Document> documents;
   private List<Image> images;

   public Scenario()
   {
      super();
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      setName(name);
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
