package org.giavacms.faq.model;

import org.giavacms.base.model.attachment.Image;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FaqCategory implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "FaqCategory";

   public FaqCategory()
   {
   }

   private Long id;
   private String name;
   private String description;
   private Image image;
   private Image newImage;
   private List<Faq> faqs;
   private int orderNum;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
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

   @OneToMany(mappedBy = "faqCategory", fetch = FetchType.LAZY)
   @OrderBy("name")
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
      return "FaqCategory [id=" + getId() + ", name=" + name + ", description="
               + description + ", orderNum=" + orderNum
               + "]";
   }

}
