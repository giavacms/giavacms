package org.giavacms.contest.model.pojo;

import java.io.Serializable;

public class Ranking implements Serializable
{

   private static final long serialVersionUID = 1L;
   private String participationId;
   private String preference;
   private int votes;

   public String getParticipationId()
   {
      return participationId;
   }

   public void setParticipationId(String participationId)
   {
      this.participationId = participationId;
   }

   public int getVotes()
   {
      return votes;
   }

   public void setVotes(int votes)
   {
      this.votes = votes;
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
