package org.giavacms.richcontent.model.type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.giavacms.base.model.Page;

@Entity
@Table(name = RichContentType.TABLE_NAME)
public class RichContentType implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "RichContentType";

   private Long id;
   private boolean active = true;
   private String name;
   private Page page;

   public RichContentType()
   {
   }

   public RichContentType(Long id)
   {
      this.id = id;
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

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @ManyToOne
   public Page getPage()
   {
      return page;
   }

   public void setPage(Page page)
   {
      this.page = page;
   }

   @Override
   public String toString()
   {
      return "RichContentType [id=" + id + ", active=" + active + ", name="
               + name
               + ", page="
               + page +
               "]";
   }

}
