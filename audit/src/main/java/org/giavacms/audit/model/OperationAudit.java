/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.audit.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class OperationAudit implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String NEW = "NEW";
   public static final String DELETE = "DELETE";
   public static final String MODIFY = "MODIFY";
   public static final String LOGIN = "LOGIN";
   public static final String LOGOUT = "LOGOUT";

   private Long id;
   private String tipo;
   private String username;
   private String descrizione;
   private Date data;

   public OperationAudit()
   {

   }

   public OperationAudit(String tipo, String username, String descrizione,
            Date data)
   {
      this.tipo = tipo;
      this.username = username;
      this.descrizione = descrizione;
      this.data = data;
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

   // NEW
   // DELETE
   // MODIFY
   // LOGIN
   // LOGOUT
   public String getTipo()
   {
      return tipo;
   }

   public void setTipo(String tipo)
   {
      this.tipo = tipo;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   @Lob
   @Column(length = 102400)
   public String getDescrizione()
   {
      return descrizione;
   }

   public void setDescrizione(String descrizione)
   {
      this.descrizione = descrizione;
   }

   public Date getData()
   {
      return data;
   }

   public void setData(Date data)
   {
      this.data = data;
   }

   @Override
   public String toString()
   {
      // TODO Auto-generated method stub
      return "";
   }
}
