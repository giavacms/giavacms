package org.giavacms.exhibition.controller;

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
import org.giavacms.exhibition.model.Sponsor;
import org.giavacms.exhibition.producer.ExhibitionProducer;
import org.giavacms.exhibition.repository.SponsorRepository;

@Named
@SessionScoped
public class SponsorController extends AbstractLazyController<Sponsor>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/exhibition/sponsor/view.xhtml";
   @ListPage
   public static String LIST = "/private/exhibition/sponsor/list.xhtml";
   @EditPage
   public static String EDIT = "/private/exhibition/sponsor/edit.xhtml";

   public static String EDIT_IMAGE = "/private/exhibition/sponsor/edit-image.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(SponsorRepository.class)
   SponsorRepository sponsorRepository;

   @Inject
   ExhibitionProducer exhibitionProducer;

   @Override
   public void initController()
   {
   }

   @Override
   public Object getId(Sponsor t)
   {
      // TODO Auto-generated method stub
      return t.getId();
   }

   @Override
   public String update()
   {
      saveImage();
      exhibitionProducer.resetItemsForClass(Sponsor.class);
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
      saveImage();
      exhibitionProducer.resetItemsForClass(Sponsor.class);
      return super.save();
   }

   public String saveAndModifyImage()
   {
      save();
      setEditMode(true);
      setReadOnlyMode(false);
      exhibitionProducer.resetItemsForClass(Sponsor.class);
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
      sponsorRepository.update(getElement());
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
   }

}
