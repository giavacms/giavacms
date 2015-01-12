package org.giavacms.twizz.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Question implements Serializable
{

   private static final long serialVersionUID = 1L;
   private Long id;
   private String text;
   private boolean correct;
   private String correctResponse;
   private Argument argument;

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

   public String getText()
   {
      return text;
   }

   public void setText(String text)
   {
      this.text = text;
   }

   public boolean isCorrect()
   {
      return correct;
   }

   public void setCorrect(boolean correct)
   {
      this.correct = correct;
   }

   @ManyToOne
   public Argument getArgument()
   {
      return argument;
   }

   public void setArgument(Argument argument)
   {
      this.argument = argument;
   }

   public String getCorrectResponse()
   {
      return correctResponse;
   }

   public void setCorrectResponse(String correctResponse)
   {
      this.correctResponse = correctResponse;
   }

}
