package org.giavacms.chalet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Entity
@Table(name = ChaletParade.TABLE_NAME)
public class ChaletParade implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "chaletparades";
   private Long id;
   private Date data;
   private String name;
   private String preference;
   List<ChaletRanking> chaletRankings;

   @Temporal(TemporalType.TIMESTAMP)
   public Date getData()
   {
      return data;
   }

   public void setData(Date data)
   {
      this.data = data;
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

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @OneToMany(mappedBy = "parade", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
   public List<ChaletRanking> getChaletRankings()
   {
      if (chaletRankings == null)
      {
         chaletRankings = new ArrayList<ChaletRanking>();
      }
      return chaletRankings;
   }

   public void setChaletRankings(List<ChaletRanking> chaletRankings)
   {
      this.chaletRankings = chaletRankings;
   }

   public ChaletParade addChaletRanking(ChaletRanking chaletRanking)
   {
      chaletRanking.setParade(this);
      this.getChaletRankings().add(chaletRanking);
      return this;
   }

   public String getPreference()
   {
      return preference;
   }

   public void setPreference(String preference)
   {
      this.preference = preference;
   }

}
