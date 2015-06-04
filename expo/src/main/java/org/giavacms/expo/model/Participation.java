package org.giavacms.expo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by fiorenzo on 04/06/15.
 */
@Entity
public class Participation implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private String artifactname;
   private String creationdate;
   private String dimensions;
   private String material;

   private String shortdescription;

   private String discipline;
   private boolean reviewed;
   private boolean delivered;
   private String note;
   private String participationtype;
   private boolean catalogue;
   private boolean contest;

   private Exhibition exhibition;
   private Artist artist;

   @Id
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getArtifactname()
   {
      return artifactname;
   }

   public void setArtifactname(String artifactname)
   {
      this.artifactname = artifactname;
   }

   public String getCreationdate()
   {
      return creationdate;
   }

   public void setCreationdate(String creationdate)
   {
      this.creationdate = creationdate;
   }

   public String getDimensions()
   {
      return dimensions;
   }

   public void setDimensions(String dimensions)
   {
      this.dimensions = dimensions;
   }

   public String getMaterial()
   {
      return material;
   }

   public void setMaterial(String material)
   {
      this.material = material;
   }

   public String getShortdescription()
   {
      return shortdescription;
   }

   public void setShortdescription(String shortdescription)
   {
      this.shortdescription = shortdescription;
   }

   public String getDiscipline()
   {
      return discipline;
   }

   public void setDiscipline(String discipline)
   {
      this.discipline = discipline;
   }

   public boolean isReviewed()
   {
      return reviewed;
   }

   public void setReviewed(boolean reviewed)
   {
      this.reviewed = reviewed;
   }

   public boolean isDelivered()
   {
      return delivered;
   }

   public void setDelivered(boolean delivered)
   {
      this.delivered = delivered;
   }

   public String getNote()
   {
      return note;
   }

   public void setNote(String note)
   {
      this.note = note;
   }

   public String getParticipationtype()
   {
      return participationtype;
   }

   public void setParticipationtype(String participationtype)
   {
      this.participationtype = participationtype;
   }

   public boolean isCatalogue()
   {
      return catalogue;
   }

   public void setCatalogue(boolean catalogue)
   {
      this.catalogue = catalogue;
   }

   public boolean isContest()
   {
      return contest;
   }

   public void setContest(boolean contest)
   {
      this.contest = contest;
   }

   @ManyToOne
   public Exhibition getExhibition()
   {
      return exhibition;
   }

   public void setExhibition(Exhibition exhibition)
   {
      this.exhibition = exhibition;
   }

   @ManyToOne
   public Artist getArtist()
   {
      return artist;
   }

   public void setArtist(Artist artist)
   {
      this.artist = artist;
   }

}
