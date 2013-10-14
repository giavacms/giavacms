/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = Template.TABLE_NAME)
public class Template implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String TABLE_NAME = "Template";

   // ------------------------------------------------------------------------

   private Long id;
   boolean active = true;

   private String name;

   private String header_start;
   private String header_stop;

   private String footer_start;
   private String footer_stop;

   private String col1_start;
   private String col1_stop;

   private String col2_start;
   private String col2_stop;

   private String col3_start;
   private String col3_stop;

   private boolean statico = true;

   private Boolean searchStatico;

   // ------------------------------------------------------------------------

   public Template()
   {
   }

   // ------------------------------------------------------------------------

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

   @Lob
   @Column(length = 100 * 1024)
   public String getHeader_start()
   {
      return header_start;
   }

   public void setHeader_start(String headerStart)
   {
      header_start = headerStart;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getHeader_stop()
   {
      return header_stop;
   }

   public void setHeader_stop(String headerStop)
   {
      header_stop = headerStop;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getFooter_start()
   {
      return footer_start;
   }

   public void setFooter_start(String footerStart)
   {
      footer_start = footerStart;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getFooter_stop()
   {
      return footer_stop;
   }

   public void setFooter_stop(String footerStop)
   {
      footer_stop = footerStop;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getCol1_start()
   {
      return col1_start;
   }

   public void setCol1_start(String col1Start)
   {
      col1_start = col1Start;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getCol1_stop()
   {
      return col1_stop;
   }

   public void setCol1_stop(String col1Stop)
   {
      col1_stop = col1Stop;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getCol2_start()
   {
      return col2_start;
   }

   public void setCol2_start(String col2Start)
   {
      col2_start = col2Start;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getCol2_stop()
   {
      return col2_stop;
   }

   public void setCol2_stop(String col2Stop)
   {
      col2_stop = col2Stop;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getCol3_start()
   {
      return col3_start;
   }

   public void setCol3_start(String col3Start)
   {
      col3_start = col3Start;
   }

   @Lob
   @Column(length = 100 * 1024)
   public String getCol3_stop()
   {
      return col3_stop;
   }

   public void setCol3_stop(String col3Stop)
   {
      col3_stop = col3Stop;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   public boolean getStatico()
   {
      return this.statico;
   }

   public void setStatico(boolean s)
   {
      this.statico = s;
   }

   @Transient
   public Boolean getSearchStatico()
   {
      return searchStatico;
   }

   public void setSearchStatico(Boolean searchStatico)
   {
      this.searchStatico = searchStatico;
   }

   @Override
   public String toString()
   {
      return "Template [id=" + id + ", active=" + active + ", name=" + name
               + ", header_start=" + header_start + ", header_stop="
               + header_stop + ", footer_start=" + footer_start
               + ", footer_stop=" + footer_stop + ", col1_start=" + col1_start
               + ", col1_stop=" + col1_stop + ", col2_start=" + col2_start
               + ", col2_stop=" + col2_stop + ", col3_start=" + col3_start
               + ", col3_stop=" + col3_stop + ", statico=" + statico
               + ", searchStatico=" + searchStatico + "]";
   }

   // ------------------------------------------------------------------------

   // @Override
   // public String toString() {
   // return ( this.nome != null ) ? this.nome : super.toString();
   // }
   //
   // @Override
   // public boolean equals(Object o) {
   // if ( ! ( o instanceof Page ) )
   // return false;
   // Page p = (Page)o;
   // return p.getId() == null ? false : p.getId().equals(this.id);
   // }
   //
   // @Override
   // public int hashCode() {
   // return ( this.id != null ) ? this.id.hashCode() : super.hashCode();
   // }

}
