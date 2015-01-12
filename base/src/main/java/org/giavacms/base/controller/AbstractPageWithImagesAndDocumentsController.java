package org.giavacms.base.controller;

import java.util.List;

import javax.inject.Inject;

import org.giavacms.base.annotation.DefaultResourceController;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.pojo.Resource;
import org.giavacms.common.util.MimeUtils;
import org.primefaces.event.FileUploadEvent;

abstract public class AbstractPageWithImagesAndDocumentsController<T extends Page> extends AbstractPageController<T>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @Inject
   @DefaultResourceController
   ResourceController resourceController;

   private String resourceUrl;

   // --------------------------------------------------------

   public AbstractPageWithImagesAndDocumentsController()
   {
   }

   // --------------------------------------------------------

   protected abstract List<Document> getElementDocuments();

   protected abstract List<Image> getElementImages();

   protected abstract String editDocsPage();

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
      getElementDocuments().add(doc);
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
         getElementImages().add(img);
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
      if (order != null && order >= 0 && getElement() != null
               && getElementDocuments() != null
               && getElementDocuments().size() > 0 && getElementDocuments().size() > order)
      {
         Document toRemove = getElementDocuments().get(order);
         getElementDocuments().remove(toRemove);
         if (toRemove.getId() != null)
         {
            getRepository().update(getElement());
         }
      }
      else
      {
         logger.info("removeDocument: non posso rimuovere posizione :" + order);
      }
   }

   public void removeImage(Long order)
   {
      removeImage(order.intValue());
   }

   public void removeImage(Integer order)
   {
      if (order != null && order >= 0 && getElement() != null
               && getElementImages() != null
               && getElementImages().size() > 0 && getElementImages().size() > order)
      {
         Image toRemove = getElementImages().get(order);
         getElementImages().remove(toRemove);
         if (toRemove.getId() != null)
         {
            getRepository().update(getElement());
         }
      }
      else
         logger.info("removeImage: non posso rimuovere posizione:" + order);
   }

   public String modDocumentsCurrent()
   {
      // TODO Auto-generated method stub
      super.modCurrent();
      return editDocsPage() + REDIRECT_PARAM;
   }

   public String modDocuments()
   {
      super.modElement();
      return editDocsPage() + REDIRECT_PARAM;
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

   public String updateAndModDocuments()
   {
      String outcome = update();
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
      Resource resource = resourceController.getModel().getRowData();
      switch (resource.getResourceType())
      {
      case IMAGE:
         byte[] imgRes = resource.getBytes();
         Image img = new Image();
         img.setData(imgRes);
         img.setType(MimeUtils.getContentType(resource.getName()));
         img.setFilename(resource.getName());
         getElementImages().add(img);
         break;
      case DOCUMENT:
         byte[] docRes = resource.getBytes();
         Document doc = new Document();
         doc.setData(docRes);
         doc.setType(MimeUtils.getContentType(resource.getName()));
         doc.setFilename(resource.getName());
         getElementDocuments().add(doc);
         break;
      default:
         break;
      }
      return "";
   }

   protected void cloneDependencies(T original, T clone)
   {
      List<Document> documents = getDocuments(original);
      List<Image> images = getImages(original);

      for (Document document : documents)
      {
         document.setId(null);
         addDocument(clone, document);
      }
      for (Image image : images)
      {
         image.setId(null);
         addImage(clone, image);
      }
   }

   abstract protected void addImage(T clone, Image image);

   abstract protected void addDocument(T clone, Document document);

   abstract protected List<Image> getImages(T original);

   abstract protected List<Document> getDocuments(T original);

   public String getResourceUrl()
   {
      return resourceUrl;
   }

   public void setResourceUrl(String resourceUrl)
   {
      this.resourceUrl = resourceUrl;
   }

   public void addImageUrl()
   {
      addResource(ResourceType.IMAGE);
   }

   public void addDocumentUrl()
   {
      addResource(ResourceType.DOCUMENT);
   }

   private void addResource(ResourceType resourceType)
   {
      switch (resourceType)
      {
      case IMAGE:
         Image img = new Image();
         img.setFilename(resourceUrl);
         img.setType(null);
         getElementImages().add(img);
         break;
      case DOCUMENT:
         Document doc = new Document();
         doc.setFilename(resourceUrl);
         doc.setType(null);
         getElementDocuments().add(doc);
         break;
      default:
         break;
      }
      this.resourceUrl = null;
   }
}
