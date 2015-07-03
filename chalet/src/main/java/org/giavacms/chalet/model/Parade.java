package org.giavacms.chalet.model;

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

/**
 * Created by fiorenzo on 03/07/15.
 */
@Entity
public class Parade
{

   private Long id;
   private Date data;
   private String name;
   List<ChaletRanking> chaletRankings;

   public Date getData()
   {
      return data;
   }

   public void setData(Date data)
   {
      this.data = data;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
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

   @OneToMany(mappedBy = "parade", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
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

   public Parade addChaletRanking(ChaletRanking chaletRanking)
   {
      chaletRanking.setParade(this);
      this.getChaletRankings().add(chaletRanking);
      return this;
   }
}
