package org.giavacms.twizz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Partecipation implements Serializable
{

   private static final long serialVersionUID = 1L;
   private Long id;
   private QuizCompetitor quizCompetitor;
   private List<Reply> replies;
   private Integer points;
   private Date dateStart;
   private Date dateEnd;
   private LinkedList<Integer> indices;

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

   @OneToOne
   @JoinColumn(name = "QuizCompetitor_id")
   public QuizCompetitor getQuizCompetitor()
   {
      if (quizCompetitor == null)
         this.quizCompetitor = new QuizCompetitor();
      return quizCompetitor;
   }

   public void setQuizCompetitor(QuizCompetitor quizCompetitor)
   {
      this.quizCompetitor = quizCompetitor;
   }

   @OneToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "Partecipation_Reply", joinColumns = { @JoinColumn(name = "Partecipation_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "Reply_id", referencedColumnName = "ID", unique = true) })
   public List<Reply> getReplies()
   {
      if (replies == null)
         this.replies = new ArrayList<Reply>();
      return replies;
   }

   public void setReplies(List<Reply> replies)
   {
      this.replies = replies;
   }

   public void addReply(Reply reply)
   {
      getReplies().add(reply);
   }

   public Integer getPoints()
   {
      return points;
   }

   public void setPoints(Integer points)
   {
      this.points = points;
   }

   public Date getDateStart()
   {
      return dateStart;
   }

   public void setDateStart(Date dateStart)
   {
      this.dateStart = dateStart;
   }

   public Date getDateEnd()
   {
      return dateEnd;
   }

   public void setDateEnd(Date dateEnd)
   {
      this.dateEnd = dateEnd;
   }

   @Transient
   public LinkedList<Integer> getIndices()
   {
      if (indices == null)
         this.indices = new LinkedList<Integer>();
      return indices;
   }

   public void setIndices(LinkedList<Integer> indices)
   {
      this.indices = indices;
   }

   public void add(Integer index)
   {
      getIndices().add(index);
   }

   @Override
   public String toString()
   {
      return "Partecipation [id=" + id + ", quizCompetitor=" + quizCompetitor + ", points="
               + points + ", dateStart=" + dateStart + ", dateEnd=" + dateEnd + "]";
   }

}
