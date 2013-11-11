package org.giavacms.people.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateImplRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.event.ResetEvent;
import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.people.model.PeopleType;
import org.giavacms.people.producer.PeopleProducer;
import org.giavacms.people.repository.PeopleTypeRepository;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
   @OwnRepository(RichContentRepository.class)
   RichContentRepository richContentRepository;

   @Inject
   TemplateImplRepository templateImplRepository;
   @Inject
   PageRepository pageRepository;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   @Inject
   PeopleTypeRepository peopleTypeRepository;
   @Inject
   TagRepository tagRepository;
   @Inject
   PeopleProducer richContentProducer;

   @Inject
   Event<ResetEvent> resetEvent;

   private List<Group<Tag>> peopleTags;
   private CroppedImage croppedImage;
   private StreamedContent streamedContent;
   private byte[] croppedBytes;
   private double imageWidth;
   private double imageHeight;

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
      streamedContent = null;
      croppedBytes = null;
      return super.reload();
   }

   @Override
   public String reset()
   {
      peopleTags = null;
      croppedImage = null;
      streamedContent = null;
      croppedBytes = null;
      return super.reset();
   }

   // --------------------------------------------------------

   @Override
   public String addElement()
   {
      peopleTags = null;
      croppedImage = null;
      streamedContent = null;
      croppedBytes = null;
      String outcome = super.addElement();
      getElement().setDate(new Date());
      return outcome;
   }

   @Override
   public String save()
   {
      RichContentType richContentType = richContentTypeRepository.find(getElement().getRichContentType().getId());
      getElement().setRichContentType(richContentType);
      if (super.save() == null)
      {
         super.addFacesMessage("Errori nel salvataggio");
         return null;
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(), getElement().getDate());
      resetEvent.fire(new ResetEvent(RichContent.class));
      return modImageCurrent();
   }

   // --------------------------------------------------------

   public String modImageCurrent()
   {
      modCurrent();
      setDimensions();
      streamedContent = null;
      croppedImage = null;
      croppedBytes = null;
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   public String modImage()
   {
      modElement();
      setDimensions();
      streamedContent = null;
      croppedImage = null;
      croppedBytes = null;
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   // --------------------------------------------------------

   @Override
   public String update()
   {
      RichContentType richContentType = richContentTypeRepository.find(getElement().getRichContentType().getId());
      getElement().setRichContentType(richContentType);
      if (super.update() == null)
      {
         super.addFacesMessage("Errori nell'aggiornamento dei dati");
         return null;
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(), getElement().getDate());
      resetEvent.fire(new ResetEvent(RichContent.class));
      return super.viewCurrent();
   }

   @Override
   public String delete()
   {
      resetEvent.fire(new ResetEvent(RichContent.class));
      return super.delete();
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

   public CroppedImage getCroppedImage()
   {
      return croppedImage;
   }

   public void setCroppedImage(CroppedImage croppedImage)
   {
      this.croppedImage = croppedImage;
   }

   public String crop()
   {
      if (croppedImage == null)
         return null;

      @SuppressWarnings("unused")
      String extension = org.giavacms.common.util.FileUtils.getExtension(getElement().getImages().get(0).getFilename());

      ByteArrayOutputStream baos = null;
      ByteArrayInputStream bais = null;
      try
      {
         baos = new ByteArrayOutputStream();
         baos.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);

         croppedBytes = baos.toByteArray();
         bais = new ByteArrayInputStream(croppedBytes);
         streamedContent = new DefaultStreamedContent(bais);

      }
      catch (Exception e)
      {
         super.addFacesMessage("Errori nell'elaborazione dell'immagine");
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
         if (bais != null)
         {
            try
            {
               bais.close();
            }
            catch (Exception e)
            {
            }
         }
      }
      return null;
   }

   public String undoCrop()
   {
      this.streamedContent = null;
      this.croppedBytes = null;
      this.croppedImage = null;
      return null;
   }

   public String confirmCrop()
   {
      try
      {
         String type = getElement().getImages().get(0).getType();
         String original = getElement().getImages().get(0)
                  .getFilename();
         if (getElement().getImages() != null && getElement().getImages().size() > 0)
         {
            removeImage(getElement().getImages().get(0).getId());
         }
         Image img = new Image();
         img.setData(croppedBytes);
         img.setType(type);
         String filename = ResourceUtils.createImage_("img", "resized_" + original, croppedBytes);
         img.setFilename(filename);
         getElement().getImages().add(img);
      }
      catch (Exception e)
      {
         super.addFacesMessage("Errori nel salvataggio dell'immagine ritagliata");
         e.printStackTrace();
      }
      this.update();
      return viewCurrent();
   }

   public StreamedContent getStreamedContent()
   {
      return streamedContent;
   }

   public void setStreamedContent(StreamedContent streamedContent)
   {
      this.streamedContent = streamedContent;
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

   private void setDimensions()
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
