package org.giavacms.twizz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class QuizCompetitor implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private String fullName;
   private String phone;
   private String email;
   private String language;
   private Argument argument;
   private Date registrationDate;
   private Date confirmationDate;
   private Long minutes;
   private Long score;
   private Long totalTime;

   public QuizCompetitor()
   {
   }

   public QuizCompetitor(String fullName, String phone, String email, String language, String argumentId, Long minutes)
   {
      this.fullName = fullName;
      this.phone = phone;
      this.email = email;
      this.language = language;
      this.argument = new Argument();
      this.argument.setId(argumentId);
      this.registrationDate = new Date();
      this.minutes = minutes;
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

   @ManyToOne
   public Argument getArgument()
   {
      if (argument == null)
         this.argument = new Argument();
      return argument;
   }

   public void setArgument(Argument argument)
   {
      this.argument = argument;
   }

   public String getFullName()
   {
      return fullName;
   }

   public void setFullName(String fullName)
   {
      this.fullName = fullName;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
   }

   public Date getRegistrationDate()
   {
      return registrationDate;
   }

   public void setRegistrationDate(Date registrationDate)
   {
      this.registrationDate = registrationDate;
   }

   public Date getConfirmationDate()
   {
      return confirmationDate;
   }

   public void setConfirmationDate(Date confirmationDate)
   {
      this.confirmationDate = confirmationDate;
   }

   public Long getScore()
   {
      return score;
   }

   public void setScore(Long score)
   {
      this.score = score;
   }

   public Long getTotalTime()
   {
      return totalTime;
   }

   public void setTotalTime(Long totalTime)
   {
      this.totalTime = totalTime;
   }

   public Long getMinutes()
   {
      return minutes;
   }

   public void setMinutes(Long minutes)
   {
      this.minutes = minutes;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   @Override
   public String toString()
   {
      return "QuizCompetitor [id=" + id + ", fullName=" + fullName + ", phone=" + phone + ", email=" + email
               + ", language=" + language + ", argument=" + argument + ", registrationDate=" + registrationDate
               + ", confirmationDate=" + confirmationDate + ", minutes=" + minutes + ", score=" + score
               + ", totalTime=" + totalTime + "]";
   }

}
