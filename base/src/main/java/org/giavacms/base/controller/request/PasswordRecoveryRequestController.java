/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.request;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.PasswordUtils;
import org.giavacms.base.model.UserAuth;
import org.giavacms.base.repository.UserRepository;
import org.giavacms.base.service.EmailSession;
import org.giavacms.common.controller.AbstractController;


@Named
@ConversationScoped
public class PasswordRecoveryRequestController implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String renewEmail;

   public static String GRAZIE = "/thanks.xhtml";

   public PasswordRecoveryRequestController()
   {

   }

   @Inject
   EmailSession emailSession;

   @Inject
   UserRepository userRepository;

   @Inject
   private Conversation conversation;

   @PostConstruct
   public void postConstruct()
   {
      conversation.begin();
   }

   @PreDestroy
   public void preDestroy()
   {

   }

   public String startRecupero()
   {
      UserAuth userAuth = userRepository.findByUsername(getRenewEmail());
      if (userAuth == null)
      {
         FacesMessage message = new FacesMessage();
         message.setDetail("Nessun utente corrispondente all'indirizzo email fornito!");
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         message.setSummary("Utente non presente");
         FacesContext.getCurrentInstance().addMessage("", message);
         return null;
      }

      if ((userAuth.getUsername() != null)
               && (!userAuth.getUsername().isEmpty()))
      {
         String newPassword = UUID.randomUUID().toString().substring(1, 8);
         // ("" + utente.toString().hashCode()).substring(
         // 1, 8);
         String title = "Richiesta modifica password";
         String body = "La nuova password dell'utente '"
                  + userAuth.getUsername() + "' è : " + newPassword;
         String result = emailSession.sendEmail("noreply@giava.by",
                  body, title, new String[] { userAuth.getUsername() }, null,
                  new String[] { "fiorenzino@gmail.com" }, null);
         if (result != null && !result.isEmpty())
         {
            userAuth.setPassword(PasswordUtils.createPassword(newPassword));
            userRepository.update(userAuth);
            // operazioniLogHandler.save(
            // OperazioniLog.MODIFY,
            // "admin",
            // "richiesta nuova pwd utente: "
            // + this.utente.getUsername());
         }
      }
      conversation.end();
      return GRAZIE + AbstractController.REDIRECT_PARAM;
   }

   public String getRenewEmail()
   {
      return renewEmail;
   }

   public void setRenewEmail(String renewEmail)
   {
      this.renewEmail = renewEmail;
   }
}
