package org.giavacms.githubcontent.controller;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.event.ResetEvent;
import org.giavacms.githubcontent.model.GithubContentType;
import org.giavacms.githubcontent.repository.GithubContentTypeRepository;
import org.giavacms.richcontent.model.type.RichContentType;

@Named
@SessionScoped
public class GithubContentTypeController extends
         AbstractLazyController<GithubContentType>
{
   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/githubcontent/githubcontenttype/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(GithubContentTypeRepository.class)
   GithubContentTypeRepository githubContentTypeRepository;

   @Inject
   Event<ResetEvent> reseEvent;

   // --------------------------------------------------------

   public GithubContentTypeController()
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
      getElement().setRichContentType(new RichContentType());
      return null;
   }

   @Override
   public String save()
   {
      super.save();
      addElement();
      reseEvent.fire(new ResetEvent(GithubContentType.class));
      return listPage();
   }

   @Override
   public GithubContentType getElement()
   {
      if (super.getElement() == null)
      {
         setElement(new GithubContentType());
         getElement().setRichContentType(new RichContentType());
      }
      return super.getElement();
   }

   @Override
   public Object getId(GithubContentType t)
   {
      return t.getId();
   }

   @Override
   public void deleteInline()
   {
      super.deleteInline();
      reseEvent.fire(new ResetEvent(GithubContentType.class));
   }

}
