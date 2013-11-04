package org.giavacms.twizz.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.twizz.model.Partecipation;
import org.giavacms.twizz.model.pojo.CallToMake;
import org.giavacms.twizz.service.PartecipationService;
import org.giavacms.twizz.service.timer.CallerTimerService;
import org.twiliofaces.inject.notification.From;

@Named
@RequestScoped
public class ConfirmController
{

   @Inject
   @From
   String from;

   @Inject
   PartecipationService partecipationService;

   @Inject
   QuizCompetitorController registerController;

   @Inject
   CallerTimerService callerTimerService;

   static String END_POINT_URL = "http://twizz-pietraia.rhcloud.com/twiml/quiz.twiml";

   public void loadPartecipation()
   {
      System.out.println("CALLER: " + from);
      Partecipation partecipation = partecipationService.loadPartecipation(from);
      if (partecipation != null)
      {
         registerController.addPartecipation(partecipation);
         System.out.println("ok. partecipation confirmed!");

         callerTimerService.createTimer(new CallToMake(partecipation.getQuizCompetitor().getPhone(), END_POINT_URL,
                  partecipation
                           .getDateStart()));
      }
      else
         System.out.println("QuizCompetitor with this number NOT FOUND!");
   }

   public void recall(String from)
   {
      System.out.println("recall: " + from);
      if (registerController.getPartecipations().containsKey(from))
      {
         Partecipation partecipation = registerController.getPartecipations().get(from);
         System.out.println("ok. partecipation confirmed!");

         callerTimerService.createTimer(new CallToMake(partecipation.getQuizCompetitor().getPhone(), END_POINT_URL,
                  partecipation
                           .getDateStart()));

      }
      else
         System.out.println("recall with this number (" + from + ") NOT FOUND!");
   }

   public String getFrom()
   {
      return from;
   }

   public void setFrom(String from)
   {
      this.from = from;
   }
}
