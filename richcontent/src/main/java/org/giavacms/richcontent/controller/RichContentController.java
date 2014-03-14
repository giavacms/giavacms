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

   @Override
   protected void cloneFields(RichContent original, RichContent clone)
   {
      clone.setAuthor(original.getAuthor());
      clone.setPreview(original.getPreview());
      clone.setContent(original.getContent());
      clone.setDate(original.getDate()
               );
      clone.setRichContentType(original.getRichContentType());
      clone.setHighlight(clone.isHighlight());
      clone.setTags(original.getTags());
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
   protected void destoryDependencies(RichContent toDestroy)
   {
      tagRepository.set(toDestroy.getId(), new ArrayList<String>(), new Date());
   }

   protected void preUpdate()
   {
      RichContentType richContentType =
               richContentTypeRepository
                        .find(getElement().getRichContentType().getId());
      getElement().setTemplateId(richContentType
               .getPage().getTemplateId());
   }

   protected void postUpdate()
   {
      tagRepository.set(getElement().getId(), getElement().getTagList(),
               getElement().getDate());
      tags = null;
      if (getElement().isHighlight())
      {
         richContentRepository.refreshEvidenza(getElement().getId());
      }
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
      String id = getElement().getId();
      String outcome = super.delete();
      if (outcome != null)
      {
         tagRepository.set(id, new ArrayList<String>(), new Date());
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

   @Override
   protected void addImage(RichContent clone, Image image)
   {
      clone.getImages().add(image);
   }

   @Override
   protected void addDocument(RichContent clone, Document document)
   {
      clone.getDocuments().add(document);
   }

   @Override
   protected List<Image> getImages(RichContent original)
   {
      return original.getImages();
   }

   @Override
   protected List<Document> getDocuments(RichContent original)
   {
      return original.getDocuments();
   }

}