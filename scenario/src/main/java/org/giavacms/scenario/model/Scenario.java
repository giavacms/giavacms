package org.giavacms.scenario.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = Scenario.TABLE_NAME)
public class Scenario implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Scenario";
   public static final boolean HAS_DETAILS = true;
   public static final String TAG_SEPARATOR = ",";
   public static final String TABLE_FK = "Scenario_id";
   public static final String DOCUMENTS_JOINTABLE_NAME = "Scenario_Document";
   public static final String DOCUMENT_FK = "documents_id";
   public static final String IMAGES_JOINTABLE_NAME = "Scenario_Image";
   public static final String IMAGE_FK = "images_id";
   public static final String PRODUCTS_JOINTABLE_NAME = "Scenario_Product";
   public static final String PRODUCT_FK = "products_id";

   private String id;
   private boolean active;
   private String tag;
   private List<String> tagList;
   private String tags;
   private String language;
   private String name;
   private String description;
   private String preview;
   private List<Product> products;
   private List<Document> documents;
   private List<Image> images;

   public Scenario()
   {
      super();
   }

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
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
   @JoinTable(name = PRODUCTS_JOINTABLE_NAME, joinColumns = @JoinColumn(name = TABLE_FK), inverseJoinColumns = @JoinColumn(name = PRODUCT_FK))
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
   @JoinTable(name = DOCUMENTS_JOINTABLE_NAME, joinColumns = @JoinColumn(name = TABLE_FK), inverseJoinColumns = @JoinColumn(name = DOCUMENT_FK))
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
   @JoinTable(name = IMAGES_JOINTABLE_NAME, joinColumns = @JoinColumn(name = TABLE_FK), inverseJoinColumns = @JoinColumn(name = IMAGE_FK))
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

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Transient
   @JsonIgnore
   public String getTag()
   {
      return tag;
   }

   public void setTag(String tag)
   {
      this.tag = tag;
   }

   public String getTags()
   {
      return tags;
   }

   public void setTags(String tags)
   {
      this.tags = tags;
      this.tagList = null;
   }

   @Transient
   @JsonIgnore
   public List<String> getTagList()
   {
      if (tagList != null)
      {
         return tagList;
      }
      tagList = new ArrayList<String>();
      if (tags == null)
      {
         return tagList;
      }
      String[] tagArray = tags.split(TAG_SEPARATOR);
      for (String tag : tagArray)
      {
         if (tag != null && tag.trim().length() > 0)
         {
            tagList.add(tag.trim());
         }
      }
      return tagList;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

}
