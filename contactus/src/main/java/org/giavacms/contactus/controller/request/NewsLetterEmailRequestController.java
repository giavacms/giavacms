package org.giavacms.contactus.controller.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.EmailUtils;
import org.giavacms.base.service.EmailSession;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.company.controller.request.CompanyRequestController;
import org.giavacms.contactus.model.ContactUs;
import org.giavacms.contactus.model.NewsLetterEmail;
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
   CompanyRequestController companyRequestController;

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
      System.out.println("privacy: " + params.get(PARAM_PRIVACY) + " email: " + params.get(PARAM_EMAIL) + " - name: "
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

         String result = emailSession.sendEmail("noreply@giava.by",
                  "nuovo iscritto newsletter: " + params.get(PARAM_NAME).trim() + " " + params.get(PARAM_EMAIL).trim(),
                  "iscrizione newletter responsabilitacivileprofessionisti.it",
                  new String[] { companyRequestController
                           .getPrincipal().getEmailNewsletter() }, null,
                  new String[] { "fiorenzino@gmail.com" }, null);
         if (result != null && !result.isEmpty())
         {
            System.out.println("ok invio email effettuato");
            return "Grazie per esserti iscritto!";
         }
         else
         {
            System.out.println(" invio email NON effettuato");
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
