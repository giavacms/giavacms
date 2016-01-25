package org.giavacms.base.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
   private String entityType;
   private String entityId;
   private String subTypeField;
   private String subTypeValue;
   private String tagName;
   private int day;
   private int month;
   private int year;

   public Tag()
   {
   }

   public Tag(String tagName, String entityType, String entityId, int day, int month, int year)
   {
      this.tagName = tagName;
      this.entityId = entityId;
      this.entityType = entityType;
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

   public String getEntityType()
   {
      return entityType;
   }

   public void setEntityType(String entityType)
   {
      this.entityType = entityType;
   }

   public String getEntityId()
   {
      return entityId;
   }

   public void setEntityId(String entityId)
   {
      this.entityId = entityId;
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
               ", entityType=" + entityType +
               ", entityId='" + entityId + '\'' +
               // ", subTypeField=" + subTypeField +
               // ", subTypeValue='" + subTypeValue + '\'' +
               ", tagName='" + tagName + '\'' +
               ", day=" + day +
               ", month=" + month +
               ", year=" + year +
               '}';
   }

   @Transient
   public String getSubTypeField()
   {
      return subTypeField;
   }

   public void setSubTypeField(String subTypeField)
   {
      this.subTypeField = subTypeField;
   }

   @Transient
   public String getSubTypeValue()
   {
      return subTypeValue;
   }

   public void setSubTypeValue(String subTypeValue)
   {
      this.subTypeValue = subTypeValue;
   }

}
