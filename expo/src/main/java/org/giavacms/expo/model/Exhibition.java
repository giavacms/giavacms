package org.giavacms.expo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by fiorenzo on 04/06/15.
 */
@Entity
@Table(name = Exhibition.TABLE_NAME)
public class Exhibition implements Serializable
{

   public static final String TABLE_NAME = "exhibitions";

   private static final long serialVersionUID = 1L;
   private String id;
   private String name;
   private String year;
   private String website;
   private String preview;
   private String description;
   private String imageurl;
   private String catalogueurl;
   private List<Participation> participations;

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
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

   public String getYear()
   {
      return year;
   }

   public void setYear(String year)
   {
      this.year = year;
   }

   public String getWebsite()
   {
      return website;
   }

   public void setWebsite(String website)
   {
      this.website = website;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getImageurl()
   {
      return imageurl;
   }

   public void setImageurl(String imageurl)
   {
      this.imageurl = imageurl;
   }

   public String getCatalogueurl()
   {
      return catalogueurl;
   }

   public void setCatalogueurl(String catalogueurl)
   {
      this.catalogueurl = catalogueurl;
   }

   @JsonIgnore
   @OneToMany(mappedBy = "exhibition")
   public List<Participation> getParticipations()
   {
      return participations;
   }

   public void setParticipations(List<Participation> participations)
   {
      this.participations = participations;
   }

   public String getPreview()
   {
      return preview;
   }

   public void setPreview(String preview)
   {
      this.preview = preview;
   }

}
