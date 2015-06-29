package org.giavacms.chalet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.URLEncoder;

@Entity
@Table(name = Tag.TABLE_NAME)
@XmlRootElement
public class Tag implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "Tag";

   private Long id;
   private Chalet chalet;
   private String chaletId;
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
      this.chalet = new Chalet();
      this.chalet.setId(richContentId);
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
   public Chalet getChalet()
   {
      return chalet;
   }

   public void setChalet(Chalet chalet)
   {
      this.chalet = chalet;
   }

   @Column(name = "chalet_id", insertable = false, updatable = false)
   public String getChaletId()
   {
      return chaletId;
   }

   public void setChaletId(String chaletId)
   {
      this.chaletId = chaletId;
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

   @JsonIgnore
   @Transient
   public String getTagNameEscaped()
   {
      try
      {
         return URLEncoder.encode(tagName, "UTF-8");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return tagName;
      }
   }

   @Override
   public String toString()
   {
      return "Tag{" +
               "id=" + id +
               ", chalet=" + chalet +
               ", chaletId='" + chaletId + '\'' +
               ", tagName='" + tagName + '\'' +
               ", day=" + day +
               ", month=" + month +
               ", year=" + year +
               '}';
   }
}
