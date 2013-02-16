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
import org.giavacms.contactus.model.NewsLetterEmail;
import org.giavacms.contactus.repository.NewsLetterEmailRepository;

@Named
@SessionScoped
public class NewsLetterEmailController extends AbstractLazyController<NewsLetterEmail>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/contactus/newsLetteremail/lista.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(NewsLetterEmailRepository.class)
   NewsLetterEmailRepository newsLetterEmailRepository;

   // --------------------------------------------------------

   public NewsLetterEmailController()
   {
   }

   @Override
   public Object getId(NewsLetterEmail t)
   {
      // TODO Auto-generated method stub
      return t.getId();
   }

   // --------------------------------------------------------

}
