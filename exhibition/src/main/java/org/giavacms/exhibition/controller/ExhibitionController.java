package org.giavacms.exhibition.controller;

import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.exhibition.model.Exhibition;
import org.giavacms.exhibition.producer.ExhibitionProducer;
import org.giavacms.exhibition.repository.ExhibitionRepository;

@Named
@SessionScoped
public class ExhibitionController extends AbstractLazyController<Exhibition>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/exhibition/view.xhtml";
   @ListPage
   public static String LIST = "/private/exhibition/list.xhtml";
   @EditPage
   public static String EDIT = "/private/exhibition/edit.xhtml";

   public static String EDIT_IMAGE = "/private/exhibition/edit-image.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(ExhibitionRepository.class)
   ExhibitionRepository exhibitionRepository;

   @Inject
   ExhibitionProducer exhibitionProducer;

   @Override
   public void initController()
   {
   }

   @Override
   public Object getId(Exhibition t)
   {
      // TODO Auto-generated method stub
      return t.getId();
   }

   @Override
   public String update()
   {
      saveImage();
      exhibitionProducer.resetItemsForClass(Exhibition.class);
      return super.update();
   }

   public String updateAndModifyImage()
   {
      update();
      setEditMode(true);
      setReadOnlyMode(false);
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   @Override
   public String save()
   {
      if (getElement().getDate() == null)
         getElement().setDate(new Date());
      saveImage();
      exhibitionProducer.resetItemsForClass(Exhibition.class);
      return super.save();
   }

   public String saveAndModifyImage()
   {
      save();
      setEditMode(true);
      setReadOnlyMode(false);
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   @Override
   public String delete()
   {
      super.delete();
      exhibitionProducer.reset();
      return listPage();
   }

   public String deleteImg()
   {
      getElement().setImage(null);
      exhibitionRepository.update(getElement());
      return listPage();
   }

   public String deleteCatalogueImage()
   {
      getElement().setCatalogueImage(null);
      exhibitionRepository.update(getElement());
      return listPage();
   }

   public String modImage()
   {
      // TODO Auto-generated method stub
      super.modElement();
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   public String modImageCurrent()
   {
      // TODO Auto-generated method stub
      super.modCurrent();
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   private void saveImage()
   {
      if (getElement().getNewImage().getUploadedData() != null
               && getElement().getNewImage().getUploadedData().getContents() != null
               && getElement().getNewImage().getUploadedData().getFileName() != null
               && !getElement().getNewImage().getUploadedData().getFileName()
                        .isEmpty())
      {
         logger.info("carico nuova immagine: "
                  + getElement().getNewImage().getUploadedData()
                           .getFileName());
         Image img = new Image();
         img.setData(getElement().getNewImage().getUploadedData()
                  .getContents());
         img.setType(getElement().getNewImage().getUploadedData()
                  .getContentType());
         String filename = ResourceUtils.createImage_("img", getElement()
                  .getNewImage().getUploadedData().getFileName(),
                  getElement().getNewImage().getUploadedData().getContents());
         img.setFilename(filename);
         getElement().setImage(img);
         getElement().setNewImage(null);
      }
      else
      {
         logger.info("non c'e' nuova immagine");
      }
      if (getElement().getNewCatalogueImage().getUploadedData() != null
               && getElement().getNewCatalogueImage().getUploadedData()
                        .getContents() != null
               && getElement().getNewCatalogueImage().getUploadedData()
                        .getFileName() != null
               && !getElement().getNewCatalogueImage().getUploadedData()
                        .getFileName().isEmpty())
      {
         logger.info("carico nuova immagine catalogo: "
                  + getElement().getNewCatalogueImage().getUploadedData()
                           .getFileName());
         Image img = new Image();
         img.setData(getElement().getNewCatalogueImage().getUploadedData()
                  .getContents());
         img.setType(getElement().getNewCatalogueImage().getUploadedData()
                  .getContentType());
         String filename = ResourceUtils.createImage_("img", getElement()
                  .getNewCatalogueImage().getUploadedData().getFileName(),
                  getElement().getNewCatalogueImage().getUploadedData()
                           .getContents());
         img.setFilename(filename);
         getElement().setCatalogueImage(img);
         getElement().setNewCatalogueImage(null);
      }
      else
      {
         logger.info("non c'e' nuova immagine catalogo");
      }
   }
}
