package org.giavacms.chalet.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

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
}
