package org.giavacms.faq.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;

@Entity
@DiscriminatorValue(value = Faq.EXTENSION)
public class Faq extends Page
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "Faq";

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
   public String getQuestion()
   {
      return super.getTitle();
   }

   public void setQuestion(String question)
   {
      super.setTitle(question);
   }

   @Lob
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
