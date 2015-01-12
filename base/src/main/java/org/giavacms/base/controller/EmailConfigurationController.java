/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.EmailConfiguration;
import org.giavacms.base.repository.EmailConfigurationRepository;
import org.giavacms.base.service.EmailSession;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.util.JSFUtils;


@Named
@SessionScoped
public class EmailConfigurationController extends
         AbstractLazyController<EmailConfiguration>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @EditPage
   @ViewPage
   @ListPage
   public static String CONF = "/private/emailconfiguration/edit.xhtml";
   // --------------------------------------------------------

   private String emailTest;
   private String resultTest;

   @Inject
   EmailSession emailSession;

   @Inject
   @OwnRepository(EmailConfigurationRepository.class)
   EmailConfigurationRepository emailConfigurationRepository;

   public EmailConfigurationController()
   {

   }

   @Override
   public EmailConfiguration getElement()
   {
      if (super.getElement() == null)
         setElement(emailConfigurationRepository.load());
      return super.getElement();
   }

   @Override
   public String update()
   {
      super.update();
      this.resultTest = "";
      return CONF;
   }

   public String getEmailTest()
   {
      return emailTest;
   }

   public void setEmailTest(String emailTest)
   {
      this.emailTest = emailTest;
   }

   public void test()
   {
      DateFormat format = new SimpleDateFormat("dd-mm-yyyy-hh:mm");
      String[] forwardMail = null;
      if (getElement().getForwardMail() != null
               && !getElement().getForwardMail().isEmpty())
      {
         forwardMail = new String[] { getElement().getForwardMail() };
      }
      String from = "noreply@giava.by";
      String title = "[GIAVACMS] - email test";
      String body = "Hi, \n\n this is a test email send from "
               + JSFUtils.getHostPort()
               + " - date:"
               + format.format(new Date())
               + ". Sorry for the noise!\n\n  bye bye";
      String result = emailSession.sendEmail(from, body, title,
               new String[] { this.emailTest }, null, forwardMail, null);
      setResultTest(result);
   }

   public String getResultTest()
   {
      return resultTest;
   }

   public void setResultTest(String resultTest)
   {
      this.resultTest = resultTest;
   }

}
