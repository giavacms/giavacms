package org.giavacms.twizz.service.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.giavacms.twizz.controller.QuizCompetitorController;
import org.giavacms.twizz.model.pojo.CallToComplete;
import org.twiliofaces.cdi.doers.CallController;

@Stateless
@LocalBean
public class ChronoTimer
{
   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   CallController callController;

   @Resource
   TimerService timerService;

   @Inject
   QuizCompetitorController quizCompetitorController;

   public void createTimer(CallToComplete callToComplete)
   {
      logger.info(callToComplete.toString());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(callToComplete.getWhen());
      timerService.createSingleActionTimer(callToComplete.getWhen(),
               new TimerConfig(callToComplete, false));
   }

   @Timeout
   public void timeout(Timer timer)
   {
      CallToComplete callToComplete = (CallToComplete) timer.getInfo();
      logger.info(getClass().getName() + ": " + new Date() + " " + callToComplete);
      boolean result = callController.callSid(callToComplete.getCallSid()).completed();
      logger.info(" completed: " + result);
      if (result)
      {
         quizCompetitorController.end(callToComplete.getCallSid());
      }

   }
}
