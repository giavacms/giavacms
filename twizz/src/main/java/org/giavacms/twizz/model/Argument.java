package org.giavacms.twizz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public class Argument implements Serializable
{

   private static final long serialVersionUID = 1L;
   private String id;
   private String name;
   private String language;
   private List<Question> questions;

   public Argument()
   {
   }

   public Argument(String id, String name, String language)
   {
      this.id = id;
      this.name = name;
      this.language = language;
   }

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

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
   }

   @OneToMany
   public List<Question> getQuestions()
   {
      if (questions == null)
         this.questions = new ArrayList<Question>();
      return questions;
   }

   public void setQuestions(List<Question> questions)
   {
      this.questions = questions;
   }

   public void addQuestion(Question question)
   {
      getQuestions().add(question);
   }

   @Override
   public String toString()
   {
      return "Argument [id=" + id + ", name=" + name + ", language=" + language + "]";
   }

}
