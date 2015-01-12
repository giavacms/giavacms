/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.pojo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Email2Send implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String oggetto;

   private String corpo;

   private String mittente;

   private List<File> allegati;

   private List<String> destinatari;

   private List<String> bcc;

   private List<String> cc;

   public String getCorpo()
   {
      return corpo;
   }

   public void setCorpo(String corpo)
   {
      this.corpo = corpo;
   }

   public List<String> getDestinatari()
   {
      if (destinatari == null)
         this.destinatari = new ArrayList<String>();
      return destinatari;
   }

   public void addDestinatari(String indirizzo)
   {
      getDestinatari().add(indirizzo);
   }

   public void setDestinatari(List<String> destinatari)
   {
      this.destinatari = destinatari;
   }

   public String getDestinatariToString()
   {
      StringBuffer to = new StringBuffer();
      for (String nome : getDestinatari())
      {
         if (to.length() > 0)
            to.append(", ");
         to.append(nome);
      }
      return to.toString();
   }

   public List<String> getCc()
   {
      if (cc == null)
         this.cc = new ArrayList<String>();
      return cc;
   }

   public void addCc(String indirizzoCc)
   {
      getCc().add(indirizzoCc);
   }

   public void setCc(List<String> cc)
   {
      this.cc = cc;
   }

   public String getCcToString()
   {
      StringBuffer to = new StringBuffer();
      for (String nome : getCc())
      {
         if (to.length() > 0)
            to.append(", ");
         to.append(nome);
      }
      return to.toString();
   }

   public List<String> getBcc()
   {
      if (bcc == null)
         this.bcc = new ArrayList<String>();
      return bcc;
   }

   public void addBcc(String indirizzoBcc)
   {
      getBcc().add(indirizzoBcc);
   }

   public void setBcc(List<String> bcc)
   {
      this.bcc = bcc;
   }

   public String getBccToString()
   {
      StringBuffer to = new StringBuffer();
      for (String nome : getBcc())
      {
         if (to.length() > 0)
            to.append(", ");
         to.append(nome);
      }
      return to.toString();
   }

   public String getOggetto()
   {
      return oggetto;
   }

   public void setOggetto(String oggetto)
   {
      this.oggetto = oggetto;
   }

   public String getMittente()
   {
      return mittente;
   }

   public void setMittente(String mittente)
   {
      this.mittente = mittente;
   }

   public List<File> getAllegati()
   {
      if (allegati == null)
         this.allegati = new ArrayList<File>();
      return allegati;
   }

   public void setAllegati(List<File> allegati)
   {
      this.allegati = allegati;
   }

   public void addAllegati(File allegato)
   {
      getAllegati().add(allegato);
   }

}
