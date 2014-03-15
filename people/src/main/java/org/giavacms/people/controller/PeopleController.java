package org.giavacms.people.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.swing.ImageIcon;

import org.giavacms.base.common.util.ImageUtils;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.pojo.Resource;
import org.giavacms.base.repository.ResourceRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.event.ResetEvent;
import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.FileUtils;
import org.giavacms.people.model.PeopleType;
import org.giavacms.people.repository.PeopleTypeRepository;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;

@Named
@SessionScoped
public class PeopleController extends AbstractPageController<RichContent>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/people/view.xhtml";
   @ListPage
   public static String LIST = "/private/people/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/people/edit-bio.xhtml";

   public static String EDIT_IMAGE = "/private/people/edit-image.xhtml";

   // --------------------------------------------------------

   @Inject
   ResourceRepository resourceRepository;

   @Inject
   @OwnRepository(RichContentRepository.class)
   RichContentRepository richContentRepository;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   @Inject
   PeopleTypeRepository peopleTypeRepository;
   @Inject
   TagRepository tagRepository;

   private List<Group<Tag>> peopleTags;
   private CroppedImage croppedImage;
   private double imageWidth;
   private double imageHeight;
   int width, height;

   // --------------------------------------------------------

   public PeopleController()
   {
   }

   @Override
   public String getExtension()
   {
      return RichContent.EXTENSION;
   }

   @Override
   public void defaultCriteria()
   {
      List<PeopleType> peopleTypes = peopleTypeRepository.getAllList();
      if (peopleTypes != null && peopleTypes.size() > 0)
      {
         getSearch().getObj().setRichContentType(new RichContentType());
         getSearch().getObj().getRichContentType().setId(peopleTypes.get(0).getRichContentType().getId());
      }
      super.defaultCriteria();
   }

   // --------------------------------------------------------

   public void filterTag(String tagName)
   {
      getSearch().getObj().setTag(tagName);
      refreshModel();
   }

   @Produces
   @Named
   public List<Group<Tag>> getPeopleTags()
   {
      if (peopleTags == null)
      {
         if (getSearch().getObj().getRichContentType() != null
                  && getSearch().getObj().getRichContentType().getId() != null)
         {
            Search<Tag> st = new Search<Tag>(Tag.class);
            st.setGrouping("tagName");
            st.getObj().setRichContent(getSearch().getObj());
            peopleTags = tagRepository.getGroups(st, 0, 50);
         }
         else
         {
            peopleTags = new ArrayList<Group<Tag>>();
         }
      }
      return peopleTags;
   }

   public void observeReset(@Observes ResetEvent resetEvent)
   {
      if (resetEvent.getObservedClass().equals(RichContent.class))
      {
         this.peopleTags = null;
      }
   }

   @Override
   public String reload()
   {
      peopleTags = null;
      croppedImage = null;
      return super.reload();
   }

   @Override
   public String reset()
   {
      peopleTags = null;
      croppedImage = null;
      return super.reset();
   }

   // --------------------------------------------------------

   @Override
   public String addElement()
   {
      peopleTags = null;
      croppedImage = null;
      String outcome = super.addElement();
      getElement().setDate(new Date());
      return outcome;
   }

   @Override
   public String save()
   {
      RichContentType richContentType =
               richContentTypeRepository
                        .find(getElement().getRichContentType().getId());
      getElement().setTemplateId(richContentType
               .getPage().getTemplateId());
      if (super.save() == null)
      {
         super.addFacesMessage("Errori nel salvataggio");
         return null;
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(), getElement().getDate());
      return modImageCurrent();
   }

   // --------------------------------------------------------

   public String modImageCurrent()
   {
      modCurrent();
      croppedImage = null;
      setDimensions();
      setTargetDimensions();
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   public String modImage()
   {
      modElement();
      croppedImage = null;
      setDimensions();
      setTargetDimensions();
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   // --------------------------------------------------------

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
      peopleTags = null;
      if (getElement().isHighlight())
      {
         richContentRepository.refreshEvidenza(getElement().getId());
      }
   }

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

   // --------------------------------------------------------

   public void handleUpload(FileUploadEvent event)
   {
      logger.info("Uploaded: " + event.getFile().getFileName() + " - "
               + event.getFile().getContentType() + "- "
               + event.getFile().getSize());
      try
      {
         if (getElement().getImages() != null && getElement().getImages().size() > 0)
         {
            removeImage(getElement().getImages().get(0).getId());
         }

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
         super.addFacesMessage("Errori nel salvataggio dell'immagine");
         e.printStackTrace();
      }
      this.update();

   }

   public String removeImage(Long id)
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
      {
         super.addFacesMessage("Errori nella rimozione dell'immagine precedente");
         logger.warn("removeImage: non posso rimuovere id:" + id);
      }
      return viewCurrent();
   }

   // --------------------------------------------------------

   public int getWidth()
   {
      return width;
   }

   public void setWidth(int width)
   {
      this.width = width;
   }

   public int getHeight()
   {
      return height;
   }

   public void setHeight(int height)
   {
      this.height = height;
   }

   protected void setDimensions()
   {
      try
      {
         width = 0;
         height = 0;
         croppedImage = null;
         ServletContext servletContext = (ServletContext) FacesContext
                  .getCurrentInstance().getExternalContext().getContext();
         String folder = servletContext.getRealPath("") + File.separator;
         getElement().getImage().setData(
                  FileUtils.getBytesFromFile(new File(new File(folder, ResourceType.IMAGE.getFolder()), getElement()
                           .getImage().getFilename())));
         ImageIcon imageIcon = new ImageIcon(getElement().getImage().getData());
         width = imageIcon.getIconWidth();
         height = imageIcon.getIconHeight();
      }
      catch (Exception e)
      {
      }
   }

   // ---------------------------------------------

   public CroppedImage getCroppedImage()
   {
      return croppedImage;
   }

   public void setCroppedImage(CroppedImage croppedImage)
   {
      this.croppedImage = croppedImage;
   }

   public String resize()
   {
      try
      {
         if (!resourceRepository.createSubFolder(ResourceType.IMAGE, "resized"))
         {
            super.addFacesMessage("Errore durante la scrittura dei dati temporanei");
            return null;
         }
         byte[] resized = null;
         if (width == 0 || height == 0)
         {
            resized = ImageUtils.resizeImage(getElement().getImage().getData(), width == 0 ? height : width,
                     FileUtils.getExtension(getElement().getImage().getFilename()));
         }
         else
         {
            resized = ImageUtils.resizeImage(getElement().getImage().getData(), width, height,
                     FileUtils.getExtension(getElement().getImage().getFilename()));
         }
         getElement().getImage().setData(resized);
         getElement().getImage().setFilePath(
                  ResourceType.IMAGE.getFolder() + "/resized/" + getElement().getImage().getFilename());
         Resource resource = new Resource();
         resource.setBytes(getElement().getImage().getData());
         resource.setId(getElement().getImage().getFilename());
         resource.setName(getElement().getImage().getFilename());
         resource.setType(ResourceType.IMAGE.getFolder() + "/resized");
         resourceRepository.updateResource(resource);
         return "";
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         super.addFacesMessage("Errori nel ridimensionamento dell'immagine");
         return null;
      }
   }

   public String crop()
   {
      if (croppedImage == null)
         return null;

      ByteArrayOutputStream baos = null;
      try
      {
         if (!resourceRepository.createSubFolder(ResourceType.IMAGE, "cropped"))
         {
            super.addFacesMessage("Errore durante la scrittura dei dati temporanei");
            return null;
         }
         baos = new ByteArrayOutputStream();
         baos.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
         getElement().getImage().setData(baos.toByteArray());
         getElement().getImage().setFilePath(
                  ResourceType.IMAGE.getFolder() + "/cropped/" + getElement().getImage().getFilename());
         Resource resource = new Resource();
         resource.setBytes(getElement().getImage().getData());
         resource.setId(getElement().getImage().getFilename());
         resource.setName(getElement().getImage().getFilename());
         resource.setType(ResourceType.IMAGE.getFolder() + "/cropped");
         resourceRepository.updateResource(resource);
         return "";
      }
      catch (Exception e)
      {
         super.addFacesMessage("Errori nel ritaglio dell'immagine");
         logger.error(e.getMessage(), e);
      }
      finally
      {
         if (baos != null)
         {
            try
            {
               baos.close();
            }
            catch (Exception e)
            {
            }
         }
      }
      return null;
   }

   public String undo()
   {
      Resource resource = new Resource();
      resource.setBytes(getElement().getImage().getData());
      resource.setId(getElement().getImage().getFilename());
      resource.setName(getElement().getImage().getFilename());
      resource.setType(ResourceType.IMAGE.getFolder() + "/cropped");
      resourceRepository.delete(resource);
      resource.setType(ResourceType.IMAGE.getFolder() + "/resized");
      resourceRepository.delete(resource);
      getElement().getImage().setFilePath(null);
      setDimensions();
      return modImageCurrent();
   }

   public String confirm()
   {
      Resource resource = new Resource();
      resource.setBytes(getElement().getImage().getData());
      resource.setId(getElement().getImage().getFilename());
      resource.setName(getElement().getImage().getFilename());
      resource.setType(ResourceType.IMAGE.getFolder() + "/cropped");
      resourceRepository.delete(resource);
      resource.setType(ResourceType.IMAGE.getFolder() + "/resized");
      resourceRepository.delete(resource);

      resource.setBytes(getElement().getImage().getData());
      resource.setId(getElement().getImage().getFilename());
      resource.setName(getElement().getImage().getFilename());
      resource.setType(ResourceType.IMAGE.getFolder());
      resourceRepository.updateResource(resource);

      getElement().getImage().setFilePath(null);
      setDimensions();
      return modImageCurrent();
   }

   public double getImageWidth()
   {
      return imageWidth;
   }

   public void setImageWidth(double imageWidth)
   {
      this.imageWidth = imageWidth;
   }

   public double getImageHeight()
   {
      return imageHeight;
   }

   public void setImageHeight(double imageHeight)
   {
      this.imageHeight = imageHeight;
   }

   private void setTargetDimensions()
   {
      for (PeopleType pt : peopleTypeRepository.getAllList())
      {
         if (pt.getRichContentType().getId() == getElement().getRichContentType().getId())
         {
            this.imageHeight = pt.getImageHeight();
            this.imageWidth = pt.getImageWidth();
         }
      }
   }

}
