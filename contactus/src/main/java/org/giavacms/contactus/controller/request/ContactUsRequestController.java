package org.giavacms.contactus.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.registry.infomodel.TelephoneNumber;

import org.giavacms.base.service.EmailSession;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.contactus.model.ContactUs;
import org.giavacms.contactus.model.ContactUsConfiguration;
import org.giavacms.contactus.repository.ContactUsConfigurationRepository;
import org.giavacms.contactus.repository.ContactUsRepository;

/**
 * 
 * <form action="" method="post"> <h:outputText value="#{contactUsRequestController.returnMessage}"/> <br/>
 * Nome: <input name="name" type="text"/> <br/>
 * Email: <input name="email" type="text"/> <br/>
 * Message: <textarea name="message" cols="40" rows="6"></textarea> <input type="submit" name="submit" value="invia"/>
 * </form>
 * 
 * @author pisi79
 * 
 */
@Named
@RequestScoped
public class ContactUsRequestController extends
         AbstractRequestController<ContactUs> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam
   String name;
   @Inject
   @HttpParam
   String email;
   @Inject
   @HttpParam
   String message;
   @Inject
   @HttpParam
   String phone;

   @Inject
   ContactUsConfigurationRepository contactUsConfigurationRepository;

   @Inject
   @OwnRepository(ContactUsRepository.class)
   ContactUsRepository contactUsRepository;

   @Inject
   EmailSession emailSession;

   @Override
   public String getIdParam()
   {
      return null;
   }

   @Override
   public String getCurrentPageParam()
   {
      return null;
   }

   public String getReturnMessage()
   {
      if (message == null || message.isEmpty() || email == null || email.isEmpty() || phone == null || phone.isEmpty()
               || name == null || name.isEmpty())
      {

         return null;
      }

      ContactUs contactUs = new ContactUs();
      contactUs.setData(new Date());
      contactUs.setEmail(email);
      contactUs.setName(name);
      contactUs.setPhone(phone);
      contactUs.setMessage(message);
      contactUsRepository.persist(contactUs);

      String from = null;
      List<String> tos = new ArrayList<String>();
      List<String> ccs = new ArrayList<String>();
      List<String> bccs = new ArrayList<String>();

      List<ContactUsConfiguration> configurations = contactUsConfigurationRepository.getList(
               new Search<ContactUsConfiguration>(ContactUsConfiguration.class), 0, 0);

      for (ContactUsConfiguration configuration : configurations)
      {
         if (!configuration.isContactus())
         {
            continue;
         }
         if (configuration.getEmail() == null || configuration.getEmail().trim().length() == 0)
         {
            continue;
         }
         if (configuration.isFrom())
         {
            from = configuration.getEmail();
         }
         if (configuration.isTo())
         {
            tos.add(configuration.getEmail());
         }
         else if (configuration.isCc())
         {
            ccs.add(configuration.getEmail());
         }
         else if (configuration.isBcc())
         {
            bccs.add(configuration.getEmail());
         }
      }

      if (tos.size() == 0 && ccs.size() == 0 && bccs.size() == 0)
      {
         logger.info("CONTACTUS MODULE: sistema non configurato per inoltrare le email.");
         return "Grazie per averci contattato";
      }
      else if (tos.size() == 0)
      {
         tos.add("noreply@giavacms.org");
      }

      if (from == null)
      {
         from = "noreply@giavacms.org";
      }

      String result = emailSession.sendEmail(from,
               "nuovo contatto dal web: " + name.trim()
                        + " \n" + email.trim() + "\n - msg: "
                        + message,
               "nuovo contatto dal web", tos.toArray(new String[] {}),
               ccs.toArray(new String[] {}),
               bccs.toArray(new String[] {}),
               null);

      if (result != null && !result.isEmpty())
      {
         logger.info("CONTACTUS MODULE: ok invio email effettuato");
      }
      else
      {
         logger.info("CONTACTUS MODULE: invio email NON effettuato");
      }
      return "Grazie per averci contattato";
   }
}
