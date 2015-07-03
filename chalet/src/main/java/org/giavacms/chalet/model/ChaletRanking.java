package org.giavacms.chalet.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = ChaletRanking.TABLE_NAME)
public class ChaletRanking implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "ChaletRanking";
   private String chaletName;
   private String licenseNumber;
   private int votes = 0;
   private Parade parade;

   public ChaletRanking(String chaletName, String licenseNumber, int votes)
   {
      this.chaletName = chaletName;
      this.licenseNumber = licenseNumber;
      this.votes = votes;
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

   @ManyToOne
   public Parade getParade()
   {
      return parade;
   }

   public void setParade(Parade parade)
   {
      this.parade = parade;
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
