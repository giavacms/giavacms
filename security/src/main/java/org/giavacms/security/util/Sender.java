/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.util;

import org.giavacms.security.pojo.Email2Send;
import org.giavacms.security.pojo.MyMessage;
import org.giavacms.security.pojo.Server;
import org.jboss.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

public class Sender implements Serializable
{

   private static final long serialVersionUID = 1L;

   static Logger logger = Logger.getLogger(Sender.class.getName());

   public static String send(Email2Send email, Server server) throws Exception
   {
      Session session;

      if (server.isAuth())
      {
         Authenticator auth = new PopAuthenticator(server.getUser(),
                  server.getPwd());
         session = Session.getInstance(getProperties(server), auth);
      }
      else
      {
         session = Session.getInstance(getProperties(server), null);
      }
      MyMessage mailMsg = new MyMessage(session);// a new email message
      InternetAddress[] addresses = null;
      try
      {
         if (email.getDestinatari() != null
                  && email.getDestinatari().size() > 0)
         {
            addresses = InternetAddress.parse(
                     email.getDestinatariToString(), false);
            mailMsg.setRecipients(Message.RecipientType.TO, addresses);
         }
         else
         {
            throw new MessagingException(
                     "The mail message requires a 'To' address.");
         }
         if (email.getCc() != null && email.getCc().size() > 0)
         {
            addresses = InternetAddress.parse(email.getCcToString(), false);
            mailMsg.setRecipients(Message.RecipientType.CC, addresses);
         }
         if (email.getBcc() != null && email.getBcc().size() > 0)
         {
            addresses = InternetAddress
                     .parse(email.getBccToString(), false);
            mailMsg.setRecipients(Message.RecipientType.BCC, addresses);
         }
         if (email.getMittente() != null)
         {
            mailMsg.setFrom(new InternetAddress(email.getMittente()));
         }
         else
         {
            throw new MessagingException(
                     "The mail message requires a valid 'From' address.");
         }
         //
         if (email.getOggetto() != null)
            mailMsg.setSubject(email.getOggetto());
         // corpo e attachment
         if (email.getAllegati() != null && email.getAllegati().size() > 0)
         {
            BodyPart messageBodyPart = new MimeBodyPart();
            if (email.getCorpo() != null)
               messageBodyPart.setText(email.getCorpo());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            // attachment
            for (File allegato : email.getAllegati())
            {
               messageBodyPart = new MimeBodyPart();
               DataSource source = new FileDataSource(
                        allegato.getCanonicalPath());
               DataHandler dataH = new DataHandler(source);
               messageBodyPart.setDataHandler(dataH);
               messageBodyPart.setFileName(allegato.getName());
               multipart.addBodyPart(messageBodyPart);
            }
            mailMsg.setContent(multipart);
         }
         else
         {
            mailMsg.setContent(email.getCorpo(),
                     "text/plain;charset=\"UTF-8\"");
         }

         mailMsg.setId("<CN" + System.currentTimeMillis()
                  + "@giava.by/giavacms>");
         // mailMsg.addHeader("Message-ID",
         // "111111.11199295388525.provaProvaProva");

         mailMsg.setSentDate(new Date());
         mailMsg.saveChanges();
         // Finally, send the mail message; throws a 'SendFailedException'
         // if any of the message's recipients have an invalid address
         Transport.send(mailMsg);
      }
      catch (MessagingException e)
      {
         logger.error(e.toString());
         e.printStackTrace();
         return "";
      }
      catch (Exception exc)
      {
         logger.error(exc.toString());
         exc.printStackTrace();
         return "";
      }
      return mailMsg.getMessageID();
   }

   private static Properties getProperties(Server server)
   {
      // Properties properties = System.getProperties();
      Properties properties = new Properties();
      properties.put("mail.smtp.host", server.getAddress());
      if (server.isAuth())
      {
         properties.put("mail.smtp.auth", "true");
      }
      if (server.isSsl())
      {
         properties.put("mail.smtp.socketFactory.class",
                  "javax.net.ssl.SSLSocketFactory");
         properties.put("mail.transport.protocol", "smtps");
      }
      if (server.isDebug())
      {
         properties.put("mail.debug", "true");
      }
      else
      {
         properties.put("mail.debug", "false");
      }
      if (server.getPort() != null && server.getPort().compareTo("") != 0)
      {
         properties.put("mail.smtp.port", server.getPort());
         properties.put("mail.smtp.socketFactory.port", server.getPort());
         properties.put("mail.smtp.socketFactory.fallback", "false");
      }
      // properties.setProperty("mail.smtp.quitwait", "false");
      return properties;
   }

}
