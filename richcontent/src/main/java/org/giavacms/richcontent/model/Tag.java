package org.giavacms.richcontent.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Tag implements Serializable
{

   private static final long serialVersionUID = 1L;
   private Long id;
   private RichContent richContent;
   private String richContentId;
   private String tagName;
   private int day;
   private int month;
   private int year;

   public Tag()
   {
   }

   public Tag(String tagName, String richContentId, int day, int month, int year)
   {
      this.tagName = tagName;
      this.richContent = new RichContent();
      this.richContent.setId(richContentId);
      this.day = day;
      this.month = month;
      this.year = year;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   @ManyToOne
   public RichContent getRichContent()
   {
      return richContent;
   }

   public void setRichContent(RichContent richContent)
   {
      this.richContent = richContent;
   }

   @Column(insertable = false, updatable = false)
   public String getRichContentId()
   {
      return richContentId;
   }

   public void setRichContentId(String richContentId)
   {
      this.richContentId = richContentId;
   }

   public String getTagName()
   {
      return tagName;
   }

   public void setTagName(String tagName)
   {
      this.tagName = tagName;
   }

   public int getDay()
   {
      return day;
   }

   public void setDay(int day)
   {
      this.day = day;
   }

   public int getMonth()
   {
      return month;
   }

   public void setMonth(int month)
   {
      this.month = month;
   }

   public int getYear()
   {
      return year;
   }

   public void setYear(int year)
   {
      this.year = year;
   }

}
