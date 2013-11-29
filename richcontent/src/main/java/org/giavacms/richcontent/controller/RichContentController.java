package org.giavacms.richcontent.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.annotation.DefaultResourceController;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.controller.ResourceController;
import org.giavacms.base.event.LanguageEvent;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.pojo.Resource;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateImplRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.MimeUtils;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.producer.RichContentProducer;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class RichContentController extends AbstractPageController<RichContent>
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
   TemplateImplRepository templateImplRepository;
   @Inject
   PageRepository pageRepository;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   @Inject
   TagRepository tagRepository;
   @Inject
   RichContentProducer richContentProducer;

   @Inject
   @DefaultResourceController
   ResourceController resourceController;

   @Inject
   Event<LanguageEvent> languageEvent;

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

   public void handleUpload(FileUploadEvent event)
   {
      logger.info("Uploaded: " + event.getFile().getFileName() + " - "
               + event.getFile().getContentType() + "- "
               + event.getFile().getSize());
      String type = ResourceUtils.getType(event.getFile().getFileName());
      if (ResourceType.IMAGE.name().equals(type))
      {
         handleImgUpload(event);
      }
      else
      {
         handleFileUpload(event);
      }
   }

   public void handleFileUpload(FileUploadEvent event)
   {
      Document doc = new Document();
      doc.setUploadedData(event.getFile());
      doc.setData(event.getFile().getContents());
      doc.setType(event.getFile().getContentType());
      String filename = ResourceUtils.createFile_("docs", event.getFile()
               .getFileName(), event.getFile().getContents());
      doc.setFilename(filename);
      getElement().getDocuments().add(doc);
   }

   public void handleImgUpload(FileUploadEvent event)
   {
      try
      {
         byte[] imgRes = event.getFile().getContents();
         Image img = new Image();
         img.setUploadedData(event.getFile());
         img.setData(imgRes);
         img.setType(event.getFile().getContentType());
         String filename = ResourceUtils.createImage_("img", event.getFile()
                  .getFileName(), imgRes);
         img.setFilename(filename);
         getElement().getImages().add(img);
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public void removeDocument(Long order)
   {
      removeDocument(order.intValue());
   }

   public void removeDocument(Integer order)
   {
      if (order != null && order > 0 && getElement() != null
               && getElement().getDocuments() != null
               && getElement().getDocuments().size() > 0 && getElement().getDocuments().size() > order)
      {
         Document toRemove = getElement().getDocuments().get(order);
         getElement().getDocuments().remove(toRemove);
         if (toRemove.getId() != null)
         {
            richContentRepository.update(getElement());
         }
      }
      else
         logger.info("removeDocument: non posso rimuovere posizione :" + order);
   }

   public void removeImage(Long order)
   {
      removeImage(order.intValue());
   }

   public void removeImage(Integer order)
   {
      if (order != null && order > 0 && getElement() != null
               && getElement().getImages() != null
               && getElement().getImages().size() > 0 && getElement().getImages().size() > order)
      {
         Image toRemove = getElement().getImages().get(order);
         getElement().getImages().remove(toRemove);
         if (toRemove.getId() != null)
         {
            richContentRepository.update(getElement());
         }
      }
      else
         logger.info("removeImage: non posso rimuovere posizione:" + order);
   }

   // --------------------------------------------------------

   public String cloneElement()
   {
      // carico dalla lista
      viewElement();
      // clone l'elemento corrente
      return cloneCurrent();
   }

   public String cloneCurrent()
   {
      // nuovo titolo arbitrario per la copia
      String newTitle = "Copia di " + getElement().getTitle();
      // clone
      boolean cloneOk = cloneCurrent(newTitle);
      // carico per modifica o ritorno dove sono con msg di errrore
      return cloneOk ? modCurrent() : null;
   }

   private boolean cloneCurrent(String newTitle)
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
      getElement().setTemplate(richContentType
               .getPage().getTemplate());
      if (super.save() == null)
      {
         return null;
      }
      if (richContentType.getPage().getLang() > 0)
      {
         languageEvent.fire(new LanguageEvent(richContentType.getPage().getTemplateId(), richContentType.getPage()
                  .getLang(), true));
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(),
               getElement().getDate());
      richContentProducer.reset();
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
      getElement().setTemplate(
               richContentTypeRepository
                        .find(getElement().getRichContentType().getId())
                        .getPage().getTemplate());
      if (super.update() == null)
      {
         return null;
      }
      if (richContentType.getPage().getLang() > 0)
      {
         languageEvent.fire(new LanguageEvent(richContentType.getPage().getTemplateId(), richContentType.getPage()
                  .getLang(), true));
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(),
               getElement().getDate());
      tags = null;
      richContentProducer.reset();
      if (getElement().isHighlight())
      {
         richContentRepository.refreshEvidenza(getElement().getId());
      }
      return super.viewPage();
   }

   public String modDocumentsCurrent()
   {
      // TODO Auto-generated method stub
      super.modCurrent();
      return EDIT_DOCS + REDIRECT_PARAM;
   }

   public String modDocuments()
   {
      super.modElement();
      return EDIT_DOCS + REDIRECT_PARAM;
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

   public String saveAndModDocuments()
   {
      String outcome = save();
      if (outcome == null)
      {
         return null;
      }
      return modDocumentsCurrent();
   }

   public void chooseImg()
   {
      resourceController.getSearch().getObj().setResourceType(ResourceType.IMAGE);
      resourceController.reload();
   }

   public void chooseDoc()
   {
      resourceController.getSearch().getObj().setResourceType(ResourceType.DOCUMENT);
      resourceController.reload();
   }

   public String pickResource()
   {
      resourceController.modElement();
      Resource resource = resourceController.getElement();
      switch (resource.getResourceType())
      {
      case IMAGE:
         byte[] imgRes = resource.getBytes();
         Image img = new Image();
         img.setData(imgRes);
         img.setType(MimeUtils.getContentType(resource.getName()));
         img.setFilename(resource.getName());
         getElement().getImages().add(img);
         break;
      case DOCUMENT:
         byte[] docRes = resource.getBytes();
         Document doc = new Document();
         doc.setData(docRes);
         doc.setType(MimeUtils.getContentType(resource.getName()));
         doc.setFilename(resource.getName());
         getElement().getDocuments().add(doc);
         break;
      default:
         break;
      }
      return "";
   }

}