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

   public static final String PARAM_EMAIL = "email";
   public static final String PARAM_NAME = "name";
   public static final String PARAM_PRIVACY = "privacy";
   public static final String[] PARAM_NAMES = new String[] {
            PARAM_EMAIL, PARAM_NAME, PARAM_PRIVACY };
   public static final String ID_PARAM = "unused_idParam";
   public static final String CURRENT_PAGE_PARAM = "unused_currentpage";

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
   public List<ContactUs> loadPage(int startRow, int pageSize)
   {
      return null;
   }

   @Override
   public int totalSize()
   {
      return 0;
   }

   @Override
   public String[] getParamNames()
   {
      return PARAM_NAMES;
   }

   @Override
   public String getIdParam()
   {
      return ID_PARAM;
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   public String getReturnMessage()
   {
      logger.info("privacy: " + params.get(PARAM_PRIVACY) + " email: " + params.get(PARAM_EMAIL) + " - name: "
               + params.get(PARAM_NAME));
      if (params.get(PARAM_PRIVACY) == null && params.get(PARAM_EMAIL) == null && params.get(PARAM_NAME) == null)
      {
         return "";
      }
      if (params.get(PARAM_PRIVACY) != null && params.get(PARAM_PRIVACY).equals("1")
               && params.get(PARAM_EMAIL) != null
               && !params.get(PARAM_EMAIL).trim().isEmpty() && params.get(PARAM_NAME) != null
               && !params.get(PARAM_NAME).trim().isEmpty()
               && EmailUtils.isValidEmailAddress(params.get(PARAM_EMAIL).trim()))
      {
         NewsLetterEmail newsLetterEmail = new NewsLetterEmail();
         newsLetterEmail.setEmail(params.get(PARAM_EMAIL));
         newsLetterEmail.setName(params.get(PARAM_NAME));
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
                  "nuovo iscritto newsletter: " + params.get(PARAM_NAME).trim() + " " + params.get(PARAM_EMAIL).trim(),
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
         if (params.get(PARAM_PRIVACY) == null || !params.get(PARAM_PRIVACY).equals("1"))
            returnMsg.append("non hai siglato la privacy! ");
         if (params.get(PARAM_EMAIL) == null
                  || params.get(PARAM_EMAIL).trim().isEmpty())
            returnMsg.append("non hai inserito la tua email! ");
         if (params.get(PARAM_EMAIL) != null
                  && !params.get(PARAM_EMAIL).trim().isEmpty()
                  && !EmailUtils.isValidEmailAddress(params.get(PARAM_EMAIL).trim()))
            returnMsg.append("la tua email non e' valida! ");
         if (params.get(PARAM_NAME) == null
                  || params.get(PARAM_NAME).trim().isEmpty())
            returnMsg.append("non hai inserito il tuo nome! ");
         return returnMsg.toString();
      }
   }
}
