package org.giavacms.people.controller;

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
import org.giavacms.people.model.PeopleType;
import org.giavacms.people.repository.PeopleTypeRepository;
import org.giavacms.richcontent.model.type.RichContentType;

@Named
@SessionScoped
public class PeopleTypeController extends
         AbstractLazyController<PeopleType>
{
   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/peopletype/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(PeopleTypeRepository.class)
   PeopleTypeRepository peopleTypeRepository;

   @Inject
   Event<ResetEvent> reseEvent;

   // --------------------------------------------------------

   public PeopleTypeController()
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
      reseEvent.fire(new ResetEvent(PeopleType.class));
      return listPage();
   }

   @Override
   public PeopleType getElement()
   {
      if (super.getElement() == null)
      {
         setElement(new PeopleType());
         getElement().setRichContentType(new RichContentType());
      }
      return super.getElement();
   }

   @Override
   public Object getId(PeopleType t)
   {
      return t.getId();
   }

   @Override
   public void deleteInline()
   {
      super.deleteInline();
      reseEvent.fire(new ResetEvent(PeopleType.class));
   }

}
