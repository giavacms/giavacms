/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.service;

import java.io.File;
import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.Sender;
import org.giavacms.base.model.EmailConfiguration;
import org.giavacms.base.pojo.Email2Send;
import org.giavacms.base.pojo.Server;
import org.jboss.logging.Logger;


@Named
@Stateless
@LocalBean
public class EmailSession implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Logger logger = Logger.getLogger(EmailSession.class);

   @PersistenceContext
   EntityManager em;

   public String sendEmail(String from, String body, String title,
            String[] to, String[] cc, String[] bcc, File file)
   {

      Email2Send email = new Email2Send();
      email.setMittente(from);
      email.setCorpo(body);
      email.setOggetto(title);
      // GESTIRE DESTINATARIO O MITTENTE VUOTI/NULLI
      if (to != null)
      {
         for (String toDest : to)
         {
            email.addDestinatari(toDest);
         }
      }
      if (cc != null)
      {
         for (String ccDest : cc)
         {
            email.addCc(ccDest);
         }
      }
      if (bcc != null)
      {
         for (String bccDest : bcc)
         {
            email.addBcc(bccDest);
         }
      }
      if (file != null)
      {
         email.addAllegati(file);
      }
      try
      {
         String msgId = "";
         if (from != null && to != null)
         {
            msgId = Sender.send(email, getServer());
            logger.info("sendEmail: msgId:" + msgId);
         }
         else
         {
            logger.info("sendEmail: EMAIL NON INVIABILE");
         }
         return msgId;
      }
      catch (Exception e)
      {
         e.printStackTrace();

         return "";
      }

   }

   private Server getServer()
   {
      EmailConfiguration conf = getConfigurazione();
      Server server = new Server();
      server.setAddress(conf.getSmtp());
      server.setUser(conf.getUsername());
      server.setPwd(conf.getPassword());
      server.setPort("" + conf.getServerPort());
      server.setAuth(conf.isWithAuth());
      server.setSsl(conf.isWithSsl());
      server.setDebug(conf.isWithDebug());
      return server;

   }

   public EmailConfiguration getConfigurazione()
   {
      try
      {
         return em.find(EmailConfiguration.class, 1L);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }
}
