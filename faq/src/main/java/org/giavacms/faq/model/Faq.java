package org.giavacms.faq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = Faq.TABLE_NAME)
public class Faq implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Faq";
   public static final boolean HAS_DETAILS = false;

   public Faq()
   {
   }

   private Long id;
   private FaqCategory faqCategory;
   private Date date;
   private String question;
   private String answer;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

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

   public String getQuestion()
   {
      return question;
   }

   public void setQuestion(String question)
   {
      this.question = question;
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
      return "Faq [id=" + getId() + ", question=" + getQuestion() + ", answer="
               + answer + ", faqCategory=" + faqCategory + "]";
   }

}
