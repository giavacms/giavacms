package org.giavacms.chalet.model.pojo;

import java.io.Serializable;

public class ChaletRanking implements Serializable
{

   private static final long serialVersionUID = 1L;
   private String chaletName;
   private String licenseNumber;
   private int votes;

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

   @Override public String toString()
   {
      return "ChaletRanking{" +
               "chaletName='" + chaletName + '\'' +
               ", licenseNumber='" + licenseNumber + '\'' +
               ", votes=" + votes +
               '}';
   }
}
