package org.giavacms.contactus.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.EmailUtils;
import org.giavacms.base.service.EmailSession;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.contactus.model.ContactUs;
import org.giavacms.contactus.model.ContactUsConfiguration;
import org.giavacms.contactus.model.NewsLetterEmail;
import org.giavacms.contactus.repository.ContactUsConfigurationRepository;
import org.giavacms.contactus.repository.NewsLetterEmailRepository;

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
public class NewsLetterEmailRequestController extends
         AbstractRequestController<ContactUs> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("name")
   String name;
   @Inject
   @HttpParam("email")
   String email;
   @Inject
   @HttpParam("privacy")
   String privacy;

   @Inject
   @OwnRepository(NewsLetterEmailRepository.class)
   NewsLetterEmailRepository newsLetterEmailRepository;
   @Inject
   ContactUsConfigurationRepository contactUsConfigurationRepository;

   @Inject
   EmailSession emailSession;

   public NewsLetterEmailRequestController()
   {
      super();
   }

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
      logger.info("privacy: " + privacy + " email: " + email + " - name: "
               + name);
      if (privacy == null && email == null && name == null)
      {
         return "";
      }
      if (privacy != null && privacy.equals("1")
               && email != null
               && !email.trim().isEmpty() && name != null
               && !name.trim().isEmpty()
               && EmailUtils.isValidEmailAddress(email.trim()))
      {
         NewsLetterEmail newsLetterEmail = new NewsLetterEmail();
         newsLetterEmail.setEmail(email);
         newsLetterEmail.setName(name);
         newsLetterEmail.setData(new Date());
         newsLetterEmailRepository.persist(newsLetterEmail);

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
                  "nuovo iscritto newsletter: " + name.trim() + " " + email.trim(),
                  "iscrizione newletter responsabilitacivileprofessionisti.it",
                  tos.toArray(new String[] {}),
                  ccs.toArray(new String[] {}),
                  bccs.toArray(new String[] {}),
                  null);
         if (result != null && !result.isEmpty())
         {
            logger.info("ok invio email effettuato");
            return "Grazie per esserti iscritto!";
         }
         else
         {
            logger.info(" invio email NON effettuato");
            return null;
         }

      }
      else
      {
         StringBuffer returnMsg = new StringBuffer("");
         if (privacy == null || !privacy.equals("1"))
            returnMsg.append("non hai siglato la privacy! ");
         if (email == null
                  || email.trim().isEmpty())
            returnMsg.append("non hai inserito la tua email! ");
         if (email != null
                  && !email.trim().isEmpty()
                  && !EmailUtils.isValidEmailAddress(email.trim()))
            returnMsg.append("la tua email non e' valida! ");
         if (name == null
                  || name.trim().isEmpty())
            returnMsg.append("non hai inserito il tuo nome! ");
         return returnMsg.toString();
      }
   }
}
