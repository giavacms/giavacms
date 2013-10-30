package org.giavacms.contactus.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.contactus.model.ContactUs;
import org.giavacms.contactus.model.ContactUsConfiguration;
import org.giavacms.contactus.repository.ContactUsConfigurationRepository;
import org.giavacms.contactus.repository.ContactUsRepository;

@Named
@SessionScoped
public class ContactUsConfigurationController extends AbstractLazyController<ContactUsConfiguration>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/contactus/configuration.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(ContactUsConfigurationRepository.class)
   ContactUsConfigurationRepository repository;

   // --------------------------------------------------------

   public ContactUsConfigurationController()
   {
   }

   @Override
   public String addElement()
   {
      super.addElement();
      getElement().setDescription("Nuovo indirizzo...");
      getElement().setEmail("noreply@giavacms.org");
      return super.save();
   }
}
