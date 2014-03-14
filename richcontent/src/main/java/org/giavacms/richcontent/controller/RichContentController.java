package org.giavacms.richcontent.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.annotation.DefaultResourceController;
import org.giavacms.base.controller.AbstractPageWithImagesAndDocumentsController;
import org.giavacms.base.controller.ResourceController;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;

@Named
@SessionScoped
public class RichContentController extends AbstractPageWithImagesAndDocumentsController<RichContent>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/richcontent/view.xhtml";
   @ListPage
   public static String LIST = "/private/richcontent/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/richcontent/edit.xhtml";

   public static String EDIT_DOCS = "/private/richcontent/edit-documents.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(RichContentRepository.class)
   RichContentRepository richContentRepository;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   @Inject
   TagRepository tagRepository;

   @Inject
   @DefaultResourceController
   ResourceController resourceController;

   private List<Group<Tag>> tags;

   // --------------------------------------------------------

   public RichContentController()
   {
   }

   @Override
   public String getExtension()
   {
      return RichContent.EXTENSION;
   }

   // --------------------------------------------------------

   protected boolean cloneCurrent(String newTitle)
   {
      RichContent original = getElement();

      addElement();
      getElement().setAuthor(original.getAuthor());
      getElement().setClone(original.isClone());
      getElement().setContent(original.getContent());
      getElement().setDate(original.getDate()
               );
      getElement().setDescription(original.getDescription());
      getElement().setExtended(original.isExtended());
      getElement().setExtension(original.getExtension());
      getElement().setHighlight(getElement().isHighlight());
      getElement().setFormerTitle(null);
      getElement().setId(null);
      getElement().setPreview(original.getPreview());
      getElement().setRichContentType(original.getRichContentType());
      getElement().setTags(original.getTags());
      getElement().setTemplate(original.getTemplate());
      getElement().setTemplateId(original.getTemplateId());
      getElement().setTitle(newTitle);

      if (save() == null)
      {
         super.addFacesMessage("Errori durante la copia dei dati.");
         return false;
      }

      List<Document> documents = original.getDocuments();
      List<Image> images = original.getImages();
      int lang = original.getLang();

      for (Document document : documents)
      {
         document.setId(null);
         getElement().addDocument(document);
      }
      for (Image image : images)
      {
         image.setId(null);
         getElement().addImage(image);
      }
      switch (lang)
      {
      case 1:
         getElement().setLang1id(getElement().getId());
         break;
      case 2:
         getElement().setLang2id(getElement().getId());
         break;
      case 3:
         getElement().setLang3id(getElement().getId());
         break;
      case 4:
         getElement().setLang4id(getElement().getId());
         break;
      case 5:
         getElement().setLang5id(getElement().getId());
         break;
      default:
         break;
      }

      if (update() == null)
      {
         return false;
      }

      return true;

   }

   @Override
   public String save()
   {
      RichContentType richContentType =
               richContentTypeRepository
                        .find(getElement().getRichContentType().getId());
      getElement().setTemplateId(richContentType
               .getPage().getTemplate().getId());
      if (super.save() == null)
      {
         return null;
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(),
               getElement().getDate());
      tags = null;
      if (getElement().isHighlight())
      {
         richContentRepository.refreshEvidenza(getElement().getId());
      }
      return super.viewPage();
   }

   @Override
   public String update()
   {
      // gestisco il cambio titolo come un clone del corrente piu' cancellazione del vecchio
      if (getElement().getFormerTitle() != null && !getElement().getFormerTitle().equals(getElement().getTitle()))
      {
         // veccho da cancellare
         RichContent toDelete = getElement();
         // clonazione
         boolean cloneOk = cloneCurrent(getElement().getTitle());
         // eliminazione del vecchio o msg errore
         if (cloneOk)
         {
            getRepository().delete(toDelete.getId());
            tagRepository.set(toDelete.getId(), new ArrayList<String>(), new Date());
            return viewCurrent();
         }
         else
         {
            return null;
         }
      }

      // altrimenti normale update
      RichContentType richContentType =
               richContentTypeRepository
                        .find(getElement().getRichContentType().getId());
      getElement().setTemplateId(richContentType
               .getPage().getTemplateId());
      if (super.update() == null)
      {
         return null;
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(),
               getElement().getDate());
      tags = null;
      if (getElement().isHighlight())
      {
         richContentRepository.refreshEvidenza(getElement().getId());
      }
      return super.viewPage();
   }

   public void filterTag(String tagName)
   {
      getSearch().getObj().setTag(tagName);
      refreshModel();
   }

   @Produces
   @Named
   public List<Group<Tag>> getTags()
   {
      if (tags == null)
      {
         Search<Tag> st = new Search<Tag>(Tag.class);
         st.setGrouping("tagName");
         st.getObj().setRichContent(getSearch().getObj());
         tags = tagRepository.getGroups(st, 0, 50);
      }
      return tags;
   }

   @Override
   public String reload()
   {
      tags = null;
      return super.reload();
   }

   @Override
   public String reset()
   {
      tags = null;
      return super.reset();
   }

   @Override
   public String delete()
   {
      String outcome = super.delete();
      if (outcome != null)
      {
         tagRepository.set(getElement().getId(), new ArrayList<String>(), new Date());
      }
      return outcome;
   }

   @Override
   protected List<Document> getElementDocuments()
   {
      return getElement().getDocuments();
   }

   @Override
   protected List<Image> getElementImages()
   {
      return getElement().getImages();
   }

   @Override
   public String editDocsPage()
   {
      return EDIT_DOCS;
   }

}