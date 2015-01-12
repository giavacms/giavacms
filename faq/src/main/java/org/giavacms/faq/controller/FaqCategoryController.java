package org.giavacms.faq.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;

@Named
@SessionScoped
public class FaqCategoryController extends AbstractPageController<FaqCategory>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/faq/faqcategory/view.xhtml";
   @ListPage
   public static String LIST = "/private/faq/faqcategory/list.xhtml";
   @EditPage
   public static String EDIT = "/private/faq/faqcategory/edit.xhtml";

   public static String EDIT_IMAGE = "/private/faq/faqcategory/edit-image.xhtml";

   // ------------------------------------------------

   @Override
   public void defaultCriteria()
   {
      getSearch().getObj().setTemplate(new TemplateImpl());
   }

   @Override
   public String getExtension()
   {
      return FaqCategory.EXTENSION;
   }

   @Inject
   @OwnRepository(FaqCategoryRepository.class)
   FaqCategoryRepository faqCategoryRepository;

   @Override
   public void initController()
   {
   }

   @Override
   public String update()
   {
      saveImage();
      return super.update();
   }

   @Override
   public String save()
   {
      saveImage();
      return super.save();
   }

   public String deleteImg()
   {
      getElement().setImage(null);
      faqCategoryRepository.update(getElement());
      return listPage();
   }

   public String modImage()
   {
      super.modElement();
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   public String modImageCurrent()
   {
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

   @Override
   public Object getId(FaqCategory t)
   {
      return t.getId();
   }

}
