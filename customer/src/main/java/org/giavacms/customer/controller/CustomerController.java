/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.common.util.ImageUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.model.CustomerConfiguration;
import org.giavacms.customer.repository.CustomerConfigurationRepository;
import org.giavacms.customer.repository.CustomerRepository;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class CustomerController extends AbstractLazyController<Customer>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/customer/view.xhtml";
   @ListPage
   public static String LIST = "/private/customer/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/customer/edit.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CustomerRepository.class)
   CustomerRepository prodottiRepository;

   @Inject
   CustomerConfigurationRepository customerConfigurationRepository;

   // --------------------------------------------------------

   public void handleFileUpload(FileUploadEvent event)
   {
      logger.info("Uploaded: " + event.getFile().getFileName() + " - "
               + event.getFile().getContentType() + "- "
               + event.getFile().getSize());
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
      logger.info("Uploaded: " + event.getFile().getFileName() + " - "
               + event.getFile().getContentType() + "- "
               + event.getFile().getSize());
      try
      {
         String type = event
                  .getFile()
                  .getFileName()
                  .substring(
                           event.getFile().getFileName().lastIndexOf(".") + 1);
         CustomerConfiguration customerConfiguration = customerConfigurationRepository
                  .load();
         byte[] imgRes;
         if (customerConfiguration.isResize())
         {
            imgRes = ImageUtils.resizeImage(event.getFile().getContents(),
                     customerConfiguration.getMaxWidthOrHeight(), type);
         }
         else
         {
            imgRes = event.getFile().getContents();
         }
         Image img = new Image();
         img.setUploadedData(event.getFile());
         img.setData(imgRes);
         img.setType(event.getFile().getContentType());
         String filename = ResourceUtils.createImage_("img", event.getFile()
                  .getFileName(), imgRes);
         img.setFilename(filename);
         getElement().getImages().add(img);
      }
      catch (IOException e)
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
         prodottiRepository.update(getElement());
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
         prodottiRepository.update(getElement());
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

}
