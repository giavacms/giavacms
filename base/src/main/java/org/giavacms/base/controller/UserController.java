/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.EmailUtils;
import org.giavacms.base.common.util.PasswordUtils;
import org.giavacms.base.model.UserAuth;
import org.giavacms.base.repository.UserRepository;
import org.giavacms.base.service.EmailSession;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;


@Named
@SessionScoped
public class UserController extends AbstractLazyController<UserAuth>
{

   private static final long serialVersionUID = 1L;
   boolean withHashAlgorithm = true;

   // --------------------------------------------------------

   @BackPage
   public static final String BACK = "/private/administration.xhtml";
   @ViewPage
   public static final String VIEW = "/private/user/view.xhtml";
   @ListPage
   public static final String LIST = "/private/user/list.xhtml";
   @EditPage
   public static final String EDIT = "/private/user/edit.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(UserRepository.class)
   UserRepository userRepository;

   @Inject
   EmailSession emailSession;

   // -----------------------------------------------------
   @Override
   public String addElement()
   {
      // TODO Auto-generated method stub
      super.addElement();
      getElement().setRandom(true);
      getElement().setNewElement(true);
      getElement().setAdmin(false);
      return editPage();
   }

   @Override
   public String modElement()
   {
      super.modElement();
      getElement().verifyIfAdmin();
      return super.editPage();
   }

   @Override
   public String modCurrent()
   {
      super.modCurrent();
      getElement().verifyIfAdmin();
      return super.editPage();
   }

   public String save()
   {
      if (userRepository.findByUsername(getElement().getUsername()) != null)
      {
         FacesContext.getCurrentInstance().addMessage("",
                  new FacesMessage("Nome utente non disponibile"));
         return null;
      }
      if (!EmailUtils.isValidEmailAddress(getElement().getUsername()))
      {
         FacesContext
                  .getCurrentInstance()
                  .addMessage(
                           "",
                           new FacesMessage("Nome utente non valido",
                                    "Il nome utente deve essere costituito da un indirizzo email valido"));
         return null;
      }
      if (!controllPassword())
      {
         return null;
      }
      super.save();
      return viewPage();
   }

   public String update()
   {
      if (!getElement().isAdmin()
               && !EmailUtils.isValidEmailAddress(getElement().getUsername()))
      {
         FacesContext
                  .getCurrentInstance()
                  .addMessage(
                           "",
                           new FacesMessage("Nome utente non valido",
                                    "Il nome utente deve essere costituito da un indirizzo email valido"));
         return null;
      }
      if (getElement().isRandom())
      {
         generateRandomPasswordAndSendEmail("Accesso al portale: modifica password");
      }
      else
      {
         if (getElement().getNewPassword() != null
                  && !getElement().getNewPassword().isEmpty())
         {
            if (withHashAlgorithm)
            {
               getElement().setPassword(
                        PasswordUtils.createPassword(getElement()
                                 .getNewPassword()));
            }
            else
            {
               getElement().setPassword(getElement().getNewPassword());
            }
         }
         else
         {
            logger.info("non e' stato richiesto un cambio password per account: "
                     + getElement().getUsername());
         }
      }
      super.update();
      return viewPage();
   }

   private void generateRandomPasswordAndSendEmail(String title)
   {
      String newPassword = UUID.randomUUID().toString().substring(1, 8);
      if (withHashAlgorithm)
      {
         getElement().setPassword(PasswordUtils.createPassword(newPassword));
      }
      else
      {
         getElement().setPassword(newPassword);
      }
      String body = "La password dell'utente '" + getElement().getUsername()
               + "' è : " + newPassword;
      String result = emailSession.sendEmail("noreply@giava.by", body, title,
               new String[] { getElement().getUsername() }, null,
               new String[] { "fiorenzino@gmail.com" }, null);
      logger.info(result);
   }

   private boolean controllPassword()
   {
      if (getElement().isRandom())
      {
         generateRandomPasswordAndSendEmail("Generazione nuovo account");
      }
      else
      {
         if (getElement().getNewPassword() == null
                  || getElement().getNewPassword().isEmpty())
         {
            FacesContext
                     .getCurrentInstance()
                     .addMessage(
                              "",
                              new FacesMessage("Password non inserita",
                                       "La password deve essere inserita obbligatoriamente!"));
            return false;
         }
         else
         {
            if (withHashAlgorithm)
            {
               getElement().setPassword(
                        PasswordUtils.createPassword(getElement()
                                 .getNewPassword()));
            }
            else
            {
               getElement().setPassword(getElement().getNewPassword());
            }
         }
      }
      return true;
   }

   public String delete()
   {
      super.delete();
      return listPage();
   }

}