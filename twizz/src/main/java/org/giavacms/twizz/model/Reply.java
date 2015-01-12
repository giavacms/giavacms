package org.giavacms.twizz.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Reply implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private Question question;
   private boolean response;
   private String numberGathered;
   private Integer points;

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
   public Question getQuestion()
   {
      if (question == null)
         this.question = new Question();
      return question;
   }

   public void setQuestion(Question question)
   {
      this.question = question;
   }

   public boolean getResponse()
   {
      return response;
   }

   public void setResponse(boolean response)
   {
      this.response = response;
   }

   public String getNumberGathered()
   {
      return numberGathered;
   }

   public void setNumberGathered(String numberGathered)
   {
      this.numberGathered = numberGathered;
   }

   public Integer getPoints()
   {
      if (points == null)
         this.points = 0;
      return points;
   }

   public void setPoints(Integer points)
   {
      this.points = points;
   }

   public void addPoints()
   {
      this.points = points + 1;
   }

}
