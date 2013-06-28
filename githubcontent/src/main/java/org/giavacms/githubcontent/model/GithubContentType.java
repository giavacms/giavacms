package org.giavacms.githubcontent.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.giavacms.richcontent.model.type.RichContentType;

@Entity
public class GithubContentType implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;

   private RichContentType richContentType;

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
   @JoinColumn(name = "richContentType_id")
   public RichContentType getRichContentType()
   {
      return richContentType;
   }

   public void setRichContentType(RichContentType richContentType)
   {
      this.richContentType = richContentType;
   }

   @Transient
   public String getName()
   {
      return richContentType == null ? null : richContentType.getName();
   }

}
