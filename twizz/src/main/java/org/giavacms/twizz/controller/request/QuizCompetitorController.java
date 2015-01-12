package org.giavacms.twizz.controller.request;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.twizz.model.Partecipation;
import org.giavacms.twizz.service.PartecipationService;

@ApplicationScoped
@Named
public class QuizCompetitorController implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   PartecipationService partecipationService;

   private Map<String, String> calls;

   private Map<String, Partecipation> partecipations;

   public QuizCompetitorController()
   {
      logger.info("RegisterController: " + new Date() + " - "
               + getClass());
   }

   public Map<String, Partecipation> getPartecipations()
   {
      if (partecipations == null)
         this.partecipations = new HashMap<String, Partecipation>();
      return partecipations;
   }

   public void setPartecipations(Map<String, Partecipation> partecipations)
   {
      this.partecipations = partecipations;
   }

   public void addPartecipation(Partecipation partecipation)
   {
      getPartecipations().put(partecipation.getQuizCompetitor().getPhone(), partecipation);
   }

   public Partecipation start(String phone, String callSid)
   {
      getCalls().put(callSid, phone);
      return getPartecipations().get(phone);
   }

   public void end(String callSid)
   {
      logger.info("END CALLSID: " + callSid);
      String phone = getCalls().get(callSid);
      logger.info("END PHONE: " + phone);
      Partecipation partecipation = getPartecipations().get(phone);
      partecipationService.end(partecipation);
      getPartecipations().remove(phone);
   }

   public Map<String, String> getCalls()
   {
      if (calls == null)
         this.calls = new HashMap<String, String>();
      return calls;
   }

   public void setCalls(Map<String, String> calls)
   {
      this.calls = calls;
   }
}
