package org.giavacms.richcontent.controller;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.event.LanguageEvent;
import org.giavacms.base.event.PageEvent;
import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.event.ResetEvent;
import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.primefaces.event.RowEditEvent;

@Named
@SessionScoped
public class RichContentTypeController extends
         AbstractLazyController<RichContentType>
{
   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/richcontent/richcontenttype/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(RichContentTypeRepository.class)
   RichContentTypeRepository richContentTypeRepository;

   @Inject
   Event<LanguageEvent> languageEvent;
   @Inject
   Event<PageEvent> pageEvent;
   @Inject
   Event<ResetEvent> resetEvent;

   @Inject
   PageRepository pageRepository;

   @Inject
   RichContentRepository richContentRepository;

   // --------------------------------------------------------

   public RichContentTypeController()
   {
   }

   @Override
   public void initController()
   {
      addElement();
   }

   @Override
   public String addElement()
   {
      super.addElement();
      getElement().setPage(new Page());
      return null;
   }

   // --------------------------------------------------------

   @Override
   public String reset()
   {
      addElement();
      return super.reset();
   }

   @Override
   public String save()
   {
      getElement().setPage(pageRepository.find(getElement().getPage().getId()));
      super.save();
      if (getElement().getPage().getLang() > 0)
      {
         languageEvent.fire(new LanguageEvent(getElement().getPage().getTemplateId(), getElement().getPage()
                  .getLang(), true));
      }
      updateBasePages(getElement());
      resetEvent.fire(new ResetEvent(RichContentType.class));
      addElement();
      return super.viewPage();
   }

   @Override
   public String update()
   {
      super.update();
      if (getElement().getPage().getLang() > 0)
      {
         languageEvent.fire(new LanguageEvent(getElement().getPage().getTemplateId(), getElement().getPage()
                  .getLang(), true));
      }
      updateBasePages(getElement());
      resetEvent.fire(new ResetEvent(RichContentType.class));
      return super.viewPage();
   }

   @Override
   public void onRowEdit(RowEditEvent ree)
   {
      RichContentType t = (RichContentType) ree.getObject();
      t.setPage(pageRepository.find(t.getPage().getId()));
      getRepository().update(t);
      if (t.getPage().getLang() > 0)
      {
         languageEvent.fire(new LanguageEvent(t.getPage().getTemplateId(), t.getPage()
                  .getLang(), true));
      }
      updateBasePages(t);
      resetEvent.fire(new ResetEvent(RichContentType.class));
   }

   private void updateBasePages(RichContentType t)
   {
      Search<RichContent> sr = new Search<RichContent>(RichContent.class);
      sr.getObj().setRichContentType(t);
      for (RichContent r : richContentRepository.getList(sr, 0, 0))
      {
         r.setTemplateId(t.getPage().getTemplateId());
         richContentRepository.update(r);
         pageEvent.fire(new PageEvent(r));
         if (t.getPage().getLang() > 0)
         {
            pageRepository.updateLanguage(t.getPage().getLang(), r.getId());
         }
      }
   }

   @Override
   public RichContentType getElement()
   {
      if (super.getElement() == null)
      {
         setElement(new RichContentType());
      }
      return super.getElement();
   }

   @Override
   public Object getId(RichContentType t)
   {
      return t.getId();
   }

   @Override
   public void deleteInline()
   {
      super.deleteInline();
      resetEvent.fire(new ResetEvent(RichContentType.class));
   }

   public String getExtension()
   {
      return RichContent.EXTENSION;
   }

}
