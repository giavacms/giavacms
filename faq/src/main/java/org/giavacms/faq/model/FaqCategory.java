package org.giavacms.faq.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Image;

@Entity
@DiscriminatorValue(value = FaqCategory.EXTENSION)
@Table(name = FaqCategory.TABLE_NAME)
public class FaqCategory extends Page
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "FaqCategory";
   public static final String TABLE_NAME = "FaqCategory";
   public static final boolean HAS_DETAILS = true;

   public FaqCategory()
   {
      super();
      super.setExtension(EXTENSION);
   }

   private Image image;
   private Image newImage;
   private List<Faq> faqs;
   private int orderNum;

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

   @OneToMany(mappedBy = "faqCategory", fetch = FetchType.LAZY)
   // @OrderBy("question")
   @OrderBy("title")
   public List<Faq> getFaqs()
   {
      if (faqs == null)
         this.faqs = new ArrayList<Faq>();
      return faqs;
   }

   public void setFaqs(List<Faq> faqs)
   {
      this.faqs = faqs;
   }

   public void addFaq(Faq faq)
   {
      getFaqs().add(faq);
   }

   public int getOrderNum()
   {
      return orderNum;
   }

   public void setOrderNum(int orderNum)
   {
      this.orderNum = orderNum;
   }

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "image_id", unique = true, nullable = true, insertable = true, updatable = true)
   public Image getImage()
   {
      if (image == null)
         this.image = new Image();
      return image;
   }

   public void setImage(Image image)
   {
      this.image = image;
   }

   @Transient
   public Image getNewImage()
   {
      if (newImage == null)
         this.newImage = new Image();
      return newImage;
   }

   public void setNewImage(Image newImage)
   {
      this.newImage = newImage;
   }

   @Override
   public String toString()
   {
      return "FaqCategory [id=" + super.getId() + ", name=" + super.getTitle() + ", description="
               + super.getDescription() + ", active=" + super.isActive() + ", orderNum=" + orderNum
               + "]";
   }

}
