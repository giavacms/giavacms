package org.giavacms.twizz.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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

   public static final String[] PARAM_NAMES = new String[] { FULLNAME, EMAIL, PHONE_NUMBER, ARGUMENT, MINUTES };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";
   private QuizCompetitor quizCompetitor;

   @Inject
   @OwnRepository(QuizCompetitorRepository.class)
   QuizCompetitorRepository quizCompetitorRepository;

   public RegistrationController()
   {
   }

   public void finish()
   {
      if (getParams().get(FULLNAME) != null && !getParams().get(FULLNAME).isEmpty()
               && getParams().get(EMAIL) != null && !getParams().get(EMAIL).isEmpty()
               && getParams().get(PHONE_NUMBER) != null && !getParams().get(PHONE_NUMBER).isEmpty()
               && getParams().get(ARGUMENT) != null && !getParams().get(ARGUMENT).isEmpty())
      {
         if (quizCompetitorRepository.findByPhone(getParams().get(PHONE_NUMBER), false) == null)
         {
            Long minutes = 5L;
            if (getParams().get(MINUTES) != null && !getParams().get(MINUTES).isEmpty())
               minutes = Long.parseLong(getParams().get(MINUTES));
            quizCompetitor = new QuizCompetitor(getParams().get(FULLNAME), getParams().get(PHONE_NUMBER), getParams()
                     .get(EMAIL), "en", getParams().get(ARGUMENT), minutes);
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
         if (getParams().get(FULLNAME) == null || getParams().get(FULLNAME).isEmpty())
         {
            msg.append("FULLNAME EMPTY; ");
         }
         if (getParams().get(EMAIL) == null || getParams().get(EMAIL).isEmpty())
         {
            msg.append("EMAIL EMPTY; ");
         }
         if (getParams().get(PHONE_NUMBER) == null || getParams().get(PHONE_NUMBER).isEmpty())
         {
            msg.append("PHONE_NUMBER EMPTY; ");
         }
         if (getParams().get(ARGUMENT) == null || getParams().get(ARGUMENT).isEmpty())
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

}
