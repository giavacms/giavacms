package org.giavacms.exhibition.model.pojo;

import java.io.Serializable;

public class ParticipantExhibition implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private Long partecipant_id;
   private String subject_id;
   private String type;
   private String id;
   private String name;

   public Long getPartecipant_id()
   {
      return partecipant_id;
   }

   public void setPartecipant_id(Long partecipant_id)
   {
      this.partecipant_id = partecipant_id;
   }

   public String getSubject_id()
   {
      return subject_id;
   }

   public void setSubject_id(String subject_id)
   {
      this.subject_id = subject_id;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

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

}
