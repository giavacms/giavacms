package org.giavacms.chalet.utils;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by fiorenzo on 15/08/15.
 */
public class MailUtils
{

   //   static final Logger logger = Logger.getLogger(MailUtils.class);

   public static void sendMail(String username, String password, String from, String[] tos, String subject, String text,
            String host, String port)
   {
      System.out.println("username:" + username + ".");
      System.out.println("from:" + from + ".");
      System.out.println("subject:" + subject + ".");
      System.out.println("subject:" + subject + ".");
      System.out.println("text:" + text + ".");
      System.out.println("host:" + host + ".");
      System.out.println("port:" + port + ".");

      try
      {
         Properties props = new Properties();
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.host", host);
         props.setProperty("mail.smtp.ssl.enable", "true");
         props.setProperty("mail.smtp.ssl.required", "true");
         props.put("mail.debug", "false");
         props.put("mail.smtp.port", port);
         props.put("mail.smtp.socketFactory.port", port);
         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         props.setProperty("mail.smtp.ssl.socketFactory.fallback", "false");
         Session session = Session.getInstance(props,
                  new javax.mail.Authenticator()
                  {
                     protected PasswordAuthentication getPasswordAuthentication()
                     {
                        return new PasswordAuthentication(username, password);
                     }
                  });
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         for (String to : tos)
         {
            System.out.println("to:" + to + ".");
            message.addRecipient(Message.RecipientType.TO,
                     new InternetAddress(to));
         }
         message.setSubject(subject);
         message.setText(text);
         Transport.send(message);
         System.out.println("mail inviata");
      }
      catch (Throwable e)
      {
         e.printStackTrace();
      }
   }

   public static void main(String[] args)
   {
      sendMail("AKIAIZZAJGUINPIK7BKA", "AiL+9/HnRgFXHPqkFm60LMGUqtioWtpjNC9EkgRWD5YN",
               "votalatuaestate@mch01-germany.alicestudio.it",
               new String[] { "fiorenzino@gmail.com" },
               "ciao", "ciao",
               "email-smtp.eu-west-1.amazonaws.com",
               "465");
   }

}
