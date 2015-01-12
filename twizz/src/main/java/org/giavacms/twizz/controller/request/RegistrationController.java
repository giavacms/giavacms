package org.giavacms.twizz.controller.request;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.twizz.model.QuizCompetitor;
import org.giavacms.twizz.repository.QuizCompetitorRepository;

@RequestScoped
@Named
public class RegistrationController extends AbstractRequestController<QuizCompetitor>
         implements Serializable
{

   private static final long serialVersionUID = 1L;
   private static final String START = "/p/index";
   private static final String DEJAVU = "/p/dejavu";
   private static final String TOCONFIRM = "/p/require-confirm";

   public static final String FULLNAME = "fullname";
   public static final String EMAIL = "email";
   public static final String PHONE_NUMBER = "number";
   public static final String ARGUMENT = "argument";
   public static final String MINUTES = "minutes";

   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @HttpParam(FULLNAME)
   String fullname;

   @Inject
   @HttpParam(EMAIL)
   String email;

   @Inject
   @HttpParam(ARGUMENT)
   String argument;

   @Inject
   @HttpParam(MINUTES)
   String minutesIn;

   @Inject
   @HttpParam(PHONE_NUMBER)
   String number;

   private QuizCompetitor quizCompetitor;

   @Inject
   @OwnRepository(QuizCompetitorRepository.class)
   QuizCompetitorRepository quizCompetitorRepository;

   public RegistrationController()
   {
   }

   public void finish()
   {
      if (fullname != null && !fullname.isEmpty()
               && email != null && !email.isEmpty()
               && number != null && !number.isEmpty()
               && argument != null && !argument.isEmpty())
      {
         if (quizCompetitorRepository.findByPhone(getParams().get(PHONE_NUMBER), false) == null)
         {
            Long minutes = 5L;
            if (minutesIn != null && !minutesIn.isEmpty())
               minutes = Long.parseLong(minutesIn);
            quizCompetitor = new QuizCompetitor(fullname, number, email, "en", argument, minutes);
            // REGISTRO NUOVO QUIZCOMPETITOR
            quizCompetitorRepository.persist(quizCompetitor);
            // NON CREO UNA PARTECIPATION PER ARGUMENT SCELTO - ASPETTO CONFERMA
            try
            {
               FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(TOCONFIRM);
            }
            catch (IOException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         else
         {
            try
            {
               FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(DEJAVU);
            }
            catch (IOException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
      else
      {
         StringBuffer msg = new StringBuffer(" ");
         if (fullname == null || fullname.isEmpty())
         {
            msg.append("FULLNAME EMPTY; ");
         }
         if (email == null || email.isEmpty())
         {
            msg.append("EMAIL EMPTY; ");
         }
         if (number == null || number.isEmpty())
         {
            msg.append("PHONE_NUMBER EMPTY; ");
         }
         if (argument == null || argument.isEmpty())
         {
            msg.append("ARGUMENT EMPTY");
         }
         try
         {
            FacesContext.getCurrentInstance().getExternalContext()
                     .redirect(START + "?msg=" + URLEncoder.encode(msg.toString(), "UTF-8"));
         }
         catch (IOException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

   }

   public void reset()
   {
      try
      {
         FacesContext.getCurrentInstance().getExternalContext()
                  .redirect(START);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public QuizCompetitor getQuizCompetitor()
   {
      if (quizCompetitor == null)
         this.quizCompetitor = new QuizCompetitor();
      return quizCompetitor;
   }

   public void setQuizCompetitor(QuizCompetitor quizCompetitor)
   {
      this.quizCompetitor = quizCompetitor;
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

}
