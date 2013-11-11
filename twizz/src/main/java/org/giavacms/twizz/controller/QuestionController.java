package org.giavacms.twizz.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.twizz.model.Partecipation;
import org.giavacms.twizz.model.Reply;
import org.giavacms.twizz.model.pojo.CallToComplete;
import org.giavacms.twizz.service.timer.ChronoTimer;
import org.twiliofaces.inject.context.TwilioScope;
import org.twiliofaces.inject.notification.CallSid;
import org.twiliofaces.inject.notification.Digits;
import org.twiliofaces.inject.notification.From;
import org.twiliofaces.inject.notification.To;

@TwilioScope
@Named
public class QuestionController implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   private Partecipation element;
   private Integer index;
   private Reply current;

   @Inject
   @CallSid
   String callSid;

   @Inject
   @From
   String from;

   @Inject
   @To
   String to;

   @Inject
   @Digits
   Instance<String> digits;

   @Inject
   ChronoTimer chronoTimer;

   @Inject
   QuizCompetitorController quizCompetitorController;

   private boolean first;

   public QuestionController()
   {
      logger.info("TwilioScopeController: " + new Date() + " - "
               + getClass());

   }

   public void loadQuiz()
   {
      if (this.element == null)
      {
         this.first = true;
         Calendar cal = Calendar.getInstance();
         cal.setTime(new Date());
         cal.add(Calendar.SECOND, 45);
         chronoTimer.createTimer(new CallToComplete(callSid, to, cal.getTime()));
         this.element = quizCompetitorController.start(to, callSid);

      }
      logger.info(this.element.toString());
      evaluate();

   }

   public String getMessage()
   {
      if (first == true)
      {
         this.first = false;
         return "Welcome " + getElement().getQuizCompetitor().getFullName()
                  + ". The twizz is starting! You will be asked some questions. " +
                  "Select one to anwer true or Select three to answer false. Select 0 to repeat the question. "
                  + this.current.getQuestion().getText();
      }
      else
      {
         return this.current.getQuestion().getText() + "? ";
      }
   }

   public void evaluate()
   {
      String gather = digits.get();
      logger.info("GATHER: " + gather);
      if (gather != null && !gather.isEmpty())
      {
         if (gather.equals("1") || gather.equals("4") || gather.equals("7") || gather.equals("3") || gather.equals("6")
                  || gather.equals("9"))
         {
            // registro il valore inserito per ultimo indice a corrispondente valore
            if (gather.equals("1") || gather.equals("4") || gather.equals("7"))
               getElement().getReplies().get(index).setResponse(true);
            if (gather.equals("3") || gather.equals("6") || gather.equals("9"))
               getElement().getReplies().get(index).setResponse(false);

            getElement().getReplies().get(index).setNumberGathered(gather);
         }
         else if (gather.equals("0") || gather.equals("2") || gather.equals("5") || gather.equals("8"))
         {
            // ripeto la domanda corrente
            return;
         }

      }
      this.index = getElement().getIndices().remove();
      this.current = getElement().getReplies().get(index);
   }

   public Partecipation getElement()
   {
      return element;
   }

   public void setElement(Partecipation element)
   {
      this.element = element;
   }

   public Integer getIndex()
   {
      return index;
   }

   public void setIndex(Integer index)
   {
      this.index = index;
   }

   public Reply getCurrent()
   {
      return current;
   }

   public void setCurrent(Reply current)
   {
      this.current = current;
   }

   public boolean isFirst()
   {
      return first;
   }

   public void setFirst(boolean first)
   {
      this.first = first;
   }

}
