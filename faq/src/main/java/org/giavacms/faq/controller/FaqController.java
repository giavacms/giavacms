package org.giavacms.faq.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.repository.FaqRepository;

@Named
@SessionScoped
public class FaqController extends AbstractPageController<Faq>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/faq/view.xhtml";
   @ListPage
   public static String LIST = "/private/faq/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/faq/edit.xhtml";

   // ------------------------------------------------

   @Override
   public void defaultCriteria()
   {
      getSearch().getObj().setTemplate(new TemplateImpl());
   }

   @Inject
   @OwnRepository(FaqRepository.class)
   FaqRepository faqRepository;

   @Override
   public Object getId(Faq t)
   {
      return t.getId();
   }

   @Override
   public String getExtension()
   {
      return Faq.EXTENSION;
   }

}
