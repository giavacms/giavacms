package org.giavacms.company.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.company.model.Company;
import org.giavacms.company.repository.CompanyRepository;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Image;

@Named
@SessionScoped
public class CompanyController extends AbstractLazyController<Company>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   public static String VIEW = "/private/company/view.xhtml";

   @EditPage
   public static String NEW_OR_EDIT = "/private/company/edit.xhtml";

   public static String EDIT_IMAGE = "/private/company/edit-image.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CompanyRepository.class)
   CompanyRepository companyRepository;

   @Override
   public void initController()
   {
      Company company = companyRepository.findPrincipal();
      if (company == null)
      {
         logger.info("CREO COMPANY - non esisteva!!");
         company = new Company();
         company.setPrincipal(true);
         company = companyRepository.persist(company);
      }
   }

   @Override
   public Object getId(Company t)
   {
      // TODO Auto-generated method stub
      return t.getId();
   }

   @Override
   public Company getElement()
   {
      if (super.getElement() == null)
      {
         Company company = companyRepository.findPrincipal();
         setElement(company);
      }

      return super.getElement();
   }

   public String modImage()
   {
      // TODO Auto-generated method stub
      super.modCurrent();
      return EDIT_IMAGE + super.REDIRECT_PARAM;
   }

   @Override
   public String save()
   {
      saveImage();
      super.save();
      if (getElement().isPrincipal())
      {
         companyRepository.refreshPrincipal(getElement().getId());
      }

      return super.viewPage();
   }

   @Override
   public String delete()
   {
      return super.delete();
   }

   public String deleteImg()
   {
      getElement().setImage(null);
      companyRepository.update(getElement());
      return listPage();
   }

   public String deleteLogo()
   {
      getElement().setLogo(null);
      companyRepository.update(getElement());
      return listPage();
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
      if (getElement().getNewLogo().getUploadedData() != null
               && getElement().getNewLogo().getUploadedData().getContents() != null
               && getElement().getNewLogo().getUploadedData().getFileName() != null
               && !getElement().getNewLogo().getUploadedData().getFileName()
                        .isEmpty())
      {
         logger.info("carico nuova logo: "
                  + getElement().getNewLogo().getUploadedData().getFileName());
         Image img = new Image();
         img.setData(getElement().getNewLogo().getUploadedData()
                  .getContents());
         img.setType(getElement().getNewLogo().getUploadedData()
                  .getContentType());
         String filename = ResourceUtils.createImage_("img", getElement()
                  .getNewLogo().getUploadedData().getFileName(), getElement()
                  .getNewLogo().getUploadedData().getContents());
         img.setFilename(filename);
         getElement().setLogo(img);
         getElement().setNewLogo(null);
      }
      else
      {
         logger.info("non c'e' nuova immagine");
      }
   }

   @Override
   public String update()
   {
      saveImage();
      super.update();
      if (getElement().isPrincipal())
      {
         companyRepository.refreshPrincipal(getElement().getId());
      }
      return super.viewPage();
   }
}
