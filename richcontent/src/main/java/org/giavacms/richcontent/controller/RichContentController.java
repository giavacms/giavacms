package org.giavacms.richcontent.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.FileUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateImplRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.richcontent.model.RichContent;
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
      String type = FileUtils.getType(event.getFile().getFileName());
      if (type.equals(FileUtils.IMG))
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
      String filename = FileUtils.createFile_("docs", event.getFile()
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
         String filename = FileUtils.createImage_("img", event.getFile()
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

   public void removeDocument(Long id)
   {
      if (id != null && getElement() != null
               && getElement().getDocuments() != null
               && getElement().getDocuments().size() > 0)
      {
         List<Document> docsNew = new ArrayList<Document>();
         for (Document doc : getElement().getDocuments())
         {
            if (doc.getId() != null && !doc.getId().equals(id))
            {
               docsNew.add(doc);
            }
         }
         getElement().setDocuments(docsNew);
         richContentRepository.update(getElement());
      }
      else
         logger.info("removeImage: non posso rimuovere id:" + id);
   }

   public void removeImage(Long id)
   {
      if (id != null && getElement() != null
               && getElement().getImages() != null
               && getElement().getImages().size() > 0)
      {
         List<Image> imagesNew = new ArrayList<Image>();
         for (Image img : getElement().getImages())
         {
            if (img.getId() != null && !img.getId().equals(id))
            {
               imagesNew.add(img);
            }
         }
         getElement().setImages(imagesNew);
         richContentRepository.update(getElement());
      }
      else
         logger.info("removeImage: non posso rimuovere id:" + id);
   }

   // --------------------------------------------------------

   @Override
   public String save()
   {
      getElement().setTemplate(
               richContentTypeRepository.find(getElement().getRichContentType().getId()).getPage().getTemplate());
      if (super.save() == null)
      {
         return null;
      }
      tagRepository.set(getElement().getId(),getElement().getTagList(),getElement().getDate());
      richContentProducer.reset();
      if (getElement().isHighlight())
      {
         richContentRepository.refreshEvidenza(getElement().getId());
      }

      return super.viewPage();
   }

   @Override
   public String delete()
   {
      return super.delete();
   }

   @Override
   public String update()
   {
      getElement().setTemplate(
               richContentTypeRepository.find(getElement().getRichContentType().getId()).getPage().getTemplate());
      if (super.update() == null)
      {
         return null;
      }
      tagRepository.set(getElement().getId(),getElement().getTagList(),getElement().getDate());
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

   public void filterTag(String tagName) {
      getSearch().getObj().setTag(tagName);
      refreshModel();
   }

}
