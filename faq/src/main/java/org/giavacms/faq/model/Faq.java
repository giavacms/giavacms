package org.giavacms.faq.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;

@Entity
@DiscriminatorValue(value = Faq.EXTENSION)
@Table(name = Faq.TABLE_NAME)
public class Faq extends Page
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "Faq";
   public static final String TABLE_NAME = "Faq";
   public static final boolean HAS_DETAILS = false;

   public Faq()
   {
      super();
      super.setExtension(EXTENSION);
   }

   private FaqCategory faqCategory;
   private Date date;

   private String answer;

   @ManyToOne
   public FaqCategory getFaqCategory()
   {
      if (this.faqCategory == null)
         this.faqCategory = new FaqCategory();
      return faqCategory;
   }

   public void setFaqCategory(FaqCategory faqCategory)
   {
      this.faqCategory = faqCategory;
   }

   @Transient
   @Deprecated
   public String getQuestion()
   {
      return super.getTitle();
   }

   @Deprecated
   public void setQuestion(String question)
   {
      super.setTitle(question);
   }

   @Column(length = 1024)
   public String getAnswer()
   {
      return answer;
   }

   public void setAnswer(String answer)
   {
      this.answer = answer;
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   @Override
   public String toString()
   {
      return "Faq [id=" + super.getId() + ", question=" + super.getTitle() + ", answer="
               + answer + ", faqCategory=" + faqCategory
               + ", active=" + super.isActive() + "]";
   }

}
