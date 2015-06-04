package org.giavacms.expo.model.pojo;

import java.io.Serializable;

public class Discipline implements Serializable, Comparable<Discipline>
{

   private static final long serialVersionUID = 1L;
   private String name;
   private int participants;
   private String exhibitionId;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public int getParticipants()
   {
      return participants;
   }

   public void setParticipants(int participants)
   {
      this.participants = participants;
   }

   public String getExhibitionId()
   {
      return exhibitionId;
   }

   public void setExhibitionId(String exhibitionId)
   {
      this.exhibitionId = exhibitionId;
   }

   @Override
   public int compareTo(Discipline o)
   {
      return name == null ? Integer.MIN_VALUE : (o == null || o.name == null) ? Integer.MAX_VALUE : name
               .compareTo(o.name);
   }

}
