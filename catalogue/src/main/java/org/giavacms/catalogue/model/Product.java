package org.giavacms.catalogue.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.giavacms.api.util.IdUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = Product.TABLE_NAME)
@XmlRootElement
public class Product implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Product";
   public static final String TABLE_FK = "Product_id";
   public static final String DOCUMENTS_JOINTABLE_NAME = "Product_Document";
   public static final String DOCUMENT_FK = "documents_id";
   public static final String IMAGES_JOINTABLE_NAME = "Product_Image";
   public static final String IMAGE_FK = "images_id";
   public static final boolean HAS_DETAILS = true;
   public static final String TAG_SEPARATOR = ",";

   public Product()
   {
      super();
   }

   private String id;
   private String name;
   private String description;
   boolean active = true;

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
   private String language;

   private Map<String, String[]> vals = null;

   private String tag;
   private String tags;
   private List<String> tagList;

   // private boolean active = true; --> super.active

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

   @JsonIgnore
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
   @JsonIgnore
   public int getDocSize()
   {
      return getDocuments().size();
   }

   @Transient
   @JsonIgnore
   public Image getImage()
   {
      if (getImages() != null && getImages().size() > 0)
         return getImages().get(0);
      return null;
   }

   @JsonIgnore
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
   @JsonIgnore
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
      return "Product [id=" + getId() + ", name=" + getName()
               + ", preview=" + preview + ", description="
               + getDescription() + ", category=" + (category == null ? null : category.getName())
               + ", dimensions=" + dimensions + ", code=" + code + ", active="
               + isActive() + "]";
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

   @Transient
   @JsonIgnore
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

   @Transient
   @JsonIgnore
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

   @Transient
   @JsonIgnore
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

   @Transient
   @JsonIgnore
   public Map<String, String[]> getVals()
   {
      return vals;
   }

   public void setVals(Map<String, String[]> vals)
   {
      this.vals = vals;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
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

   public String getTags()
   {
      return tags;
   }

   public void setTags(String tags)
   {
      this.tags = tags;
      this.tagList = null;
   }

   private String tmpId;

   public String getTmpId()
   {
      if (tmpId == null)
      {
         tmpId = IdUtils.createPageId(name);
      }
      return tmpId;
   }

   public void setTmpId(String tmpId)
   {
      this.tmpId = tmpId;
   }

}
