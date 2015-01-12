/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.exhibition.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.exhibition.model.Publication;
import org.giavacms.exhibition.repository.PublicationRepository;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class PublicationController extends AbstractLazyController<Publication>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/exhibition/publication/view.xhtml";
   @ListPage
   public static String LIST = "/private/exhibition/publication/list.xhtml";
   @EditPage
   public static String EDIT = "/private/exhibition/publication/edit.xhtml";

   public static String EDIT_DOCS = "/private/exhibition/publication/edit-documents.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(PublicationRepository.class)
   PublicationRepository publicationRepository;

   @Override
   public void initController()
   {
   }

   @Override
   public Object getId(Publication t)
   {
      // TODO Auto-generated method stub
      return t.getId();
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
      @SuppressWarnings("unused")
      String type = event.getFile().getFileName()
               .substring(event.getFile().getFileName().lastIndexOf(".") + 1);

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
         publicationRepository.update(getElement());
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
         publicationRepository.update(getElement());
      }
      else
         logger.info("removeImage: non posso rimuovere id:" + id);
   }

   // --------------------------------------------------------

   @Override
   public String save()
   {
      super.save();
      return super.viewPage();
   }

   public String saveAndModDocuments()
   {
      save();
      setEditMode(true);
      setReadOnlyMode(false);
      return EDIT_DOCS + REDIRECT_PARAM;
   }

   @Override
   public String delete()
   {
      return super.delete();
   }

   @Override
   public String update()
   {
      super.update();
      return super.viewPage();
   }

   public String updateAndmodDocuments()
   {
      update();
      setEditMode(true);
      setReadOnlyMode(false);
      return EDIT_DOCS + REDIRECT_PARAM;
   }

}
