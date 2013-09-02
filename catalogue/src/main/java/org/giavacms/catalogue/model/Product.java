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
import javax.persistence.Transient;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

@Entity
@DiscriminatorValue(value = Product.EXTENSION)
public class Product extends Page implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "Product";

   public Product()
   {
      super();
      super.setExtension(EXTENSION);
   }

   // private Long id --> super.id;
   // private String name --> super.title;
   private String preview;
   // private String description --> super.description;
   private Category category;
   private String dimensions;
   private String code;
   List<Document> documents;
   List<Image> images;

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
   @Column(length = 1024)
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
               + super.getDescription() + ", category=" + category.getTitle()
               + ", dimensions=" + dimensions + ", code=" + code + ", active="
               + super.isActive() + "]";
   }

}
