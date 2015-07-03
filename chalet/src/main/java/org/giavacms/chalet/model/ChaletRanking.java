package org.giavacms.chalet.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = ChaletRanking.TABLE_NAME)
public class ChaletRanking implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "chaletrankings";
   private Long id;
   private String chaletName;
   private String licenseNumber;
   private int votes = 0;
   private int position = 0;
   private ChaletParade parade;

   public ChaletRanking()
   {
      super();
   }

   public ChaletRanking(String chaletName, String licenseNumber, int votes, int position)
   {
      this.chaletName = chaletName;
      this.licenseNumber = licenseNumber;
      this.votes = votes;
      this.position = position;
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

   public int getVotes()
   {
      return votes;
   }

   public void setVotes(int votes)
   {
      this.votes = votes;
   }

   public String getChaletName()
   {
      return chaletName;
   }

   public void setChaletName(String chaletName)
   {
      this.chaletName = chaletName;
   }

   public String getLicenseNumber()
   {
      return licenseNumber;
   }

   public void setLicenseNumber(String licenseNumber)
   {
      this.licenseNumber = licenseNumber;
   }

   @JsonIgnore
   @ManyToOne
   public ChaletParade getParade()
   {
      return parade;
   }

   public void setParade(ChaletParade parade)
   {
      this.parade = parade;
   }

   public int getPosition()
   {
      return position;
   }

   public void setPosition(int position)
   {
      this.position = position;
   }

   @Override
   public String toString()
   {
      return "ChaletRanking{" +
               "chaletName='" + chaletName + '\'' +
               ", licenseNumber='" + licenseNumber + '\'' +
               ", votes=" + votes +
               '}';
   }
}
