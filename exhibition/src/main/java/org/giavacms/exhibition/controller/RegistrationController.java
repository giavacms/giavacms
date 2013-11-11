package org.giavacms.exhibition.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.giavacms.exhibition.model.Artist;
import org.giavacms.exhibition.model.Participant;
import org.giavacms.exhibition.repository.ParticipantRepository;

import org.giavacms.common.util.JSFUtils;

@Named
@SessionScoped
public class RegistrationController implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   ParticipantRepository participantRepository;

   public static String REGISTRY_1 = "/s/iscrizione-anagrafica";
   public static String REGISTRY_2 = "/s/iscrizione-opera";
   public static String REGISTRY_3 = "/s/iscrizione-complessiva";
   public static String REGISTRY_4 = "/s/iscrizione-rivedi";

   private Participant participant;

   public String goToOeuvre()
   {
      logger.info("goToOeuvre");
      // se non ci sono errori nella parte anagrafica
      if (Artist.TYPE.equals(getParticipant().getSubject().getType()))
      {
         try
         {
            JSFUtils.redirect(REGISTRY_2);
         }
         catch (IOException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      try
      {
         JSFUtils.redirect(REGISTRY_3);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public String goToReview()
   {
      // se non ci sono errori nell'opera
      logger.info("goToReview");
      try
      {
         JSFUtils.redirect(REGISTRY_3);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();

      }
      return null;
   }

   public String save()
   {
      logger.info("save");
      // se passa la validazione del captcha
      participantRepository.persist(getParticipant());
      try
      {
         JSFUtils.redirect(REGISTRY_4);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public Participant getParticipant()
   {
      if (participant == null)
      {
         this.participant = new Participant();
         this.participant.setToControl(true);
      }
      return participant;
   }

   public void setParticipant(Participant participant)
   {
      this.participant = participant;
   }

   // commodities

   protected void addFacesMessage(String summary, String message)
   {
      addFacesMessage(null, summary, message, "");
   }

   protected void addFacesMessage(String summary)
   {
      addFacesMessage(null, summary, summary, "");
   }

   protected void addFacesMessage(Severity severity, String summary,
            String message, String forComponentId)
   {
      FacesMessage fm = new FacesMessage(message);
      fm.setSummary(summary);
      if (severity != null)
      {
         fm.setSeverity(severity);
      }
      else
      {
         fm.setSeverity(FacesMessage.SEVERITY_ERROR);
      }
      FacesContext.getCurrentInstance().addMessage(forComponentId, fm);
   }

}
