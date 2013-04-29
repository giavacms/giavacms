package org.giavacms.richcontent.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.producer.RichContentProducer;
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
   RichContentProducer richContentProducer;

   @Inject
   PageRepository pageRepository;

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
      richContentProducer.reset();
      getElement().setPage(pageRepository.find(getElement().getPage().getId()));
      super.save();
      addElement();
      return super.viewPage();
   }

   @Override
   public String update()
   {
      richContentProducer.reset();
      super.update();
      return super.viewPage();
   }

   @Override
   public void onRowEdit(RowEditEvent ree)
   {
      RichContentType t = (RichContentType) ree.getObject();
      t.setPage(pageRepository.find(t.getPage().getId()));
      getRepository().update(t);
      richContentProducer.reset();
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
      richContentProducer.reset();
      super.deleteInline();
   }

   public String getExtension()
   {
      return RichContent.EXTENSION;
   }

}
