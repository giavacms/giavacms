package org.giavacms.people.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;

@Named
@RequestScoped
public class PeopleRequestController implements Serializable
{

   private static final long serialVersionUID = 1L;

   private RichContent element;

   @Inject
   RichContentRepository richContentRepository;

   public RichContent getElement()
   {
      return element;
   }

   public void setupById(String id)
   {
      if (element == null)
      {
         this.element = richContentRepository.find(id);
      }
   }

   public void setupByTitleAndType(String title, String type)
   {
      if (element == null)
      {
         Search<RichContent> s = new Search<RichContent>(RichContent.class);
         s.getObj().setTitle(title);
         s.getObj().setRichContentType(new RichContentType());
         s.getObj().getRichContentType().setName(type);
         List<RichContent> l = richContentRepository.getList(s, 0, 1);
         if (l != null && l.size() > 0)
         {
            this.element = l.get(0);
         }
      }
   }

}
