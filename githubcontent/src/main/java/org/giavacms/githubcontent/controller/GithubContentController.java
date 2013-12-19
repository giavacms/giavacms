package org.giavacms.githubcontent.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.event.ResetEvent;
import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.githubcontent.model.GithubContentType;
import org.giavacms.githubcontent.repository.GithubContentTypeRepository;
import org.giavacms.githubcontent.util.GithubImporter;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class GithubContentController extends AbstractPageController<RichContent>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/githubcontent/view.xhtml";
   @ListPage
   public static String LIST = "/private/githubcontent/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/githubcontent/edit-url.xhtml";

   public static String EDIT_IMAGE = "/private/githubcontent/edit-image.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(RichContentRepository.class)
   RichContentRepository richContentRepository;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   @Inject
   GithubContentTypeRepository githubContentTypeRepository;
   @Inject
   TagRepository tagRepository;

   private List<Group<Tag>> githubTags;
   private String githubContent = null;

   // --------------------------------------------------------

   public GithubContentController()
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
      List<GithubContentType> githubTypes = githubContentTypeRepository.getAllList();
      if (githubTypes != null && githubTypes.size() > 0)
      {
         getSearch().getObj().setRichContentType(new RichContentType());
         getSearch().getObj().getRichContentType().setId(githubTypes.get(0).getRichContentType().getId());
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
   public List<Group<Tag>> getGithubTags()
   {
      if (githubTags == null)
      {
         if (getSearch().getObj().getRichContentType() != null
                  && getSearch().getObj().getRichContentType().getId() != null)
         {
            Search<Tag> st = new Search<Tag>(Tag.class);
            st.setGrouping("tagName");
            st.getObj().setRichContent(getSearch().getObj());
            githubTags = tagRepository.getGroups(st, 0, 50);
         }
         else
         {
            githubTags = new ArrayList<Group<Tag>>();
         }
      }
      return githubTags;
   }

   public void observeReset(@Observes ResetEvent resetEvent)
   {
      if (resetEvent.getObservedClass().equals(RichContent.class))
      {
         this.githubTags = null;
      }
   }

   @Override
   public String reload()
   {
      githubTags = null;
      githubContent = null;
      return super.reload();
   }

   @Override
   public String reset()
   {
      githubTags = null;
      githubContent = null;
      return super.reset();
   }

   // --------------------------------------------------------

   @Override
   public String addElement()
   {
      githubTags = null;
      githubContent = null;
      String outcome = super.addElement();
      getElement().setDate(new Date());
      return outcome;
   }

   @Override
   public String save()
   {
      getElement().setTemplate(
               richContentTypeRepository.find(getElement().getRichContentType().getId()).getPage().getTemplate());
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
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   public String modImage()
   {
      modElement();
      return EDIT_IMAGE + REDIRECT_PARAM;
   }

   // --------------------------------------------------------

   @Override
   public String update()
   {
      getElement().setTemplate(
               richContentTypeRepository.find(getElement().getRichContentType().getId()).getPage().getTemplate());
      if (super.update() == null)
      {
         super.addFacesMessage("Errori nell'aggiornamento dei dati");
         return null;
      }
      tagRepository.set(getElement().getId(), getElement().getTagList(), getElement().getDate());
      return viewCurrent();
   }

   @Override
   public String delete()
   {
      githubContent = null;
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

   // -----------------------------------------------------------------------

   public String getGithubContent()
   {
      return githubContent;
   }

   public void setGithubContent(String githubContent)
   {
      this.githubContent = githubContent;
   }

   @Override
   public String viewElement()
   {
      String outcome = super.viewElement();
      this.githubContent = GithubImporter.getContent(getElement().getContent());
      return outcome;
   }

   @Override
   public String viewCurrent()
   {
      String outcome = super.viewCurrent();
      this.githubContent = GithubImporter.getContent(getElement().getContent());
      return outcome;
   }

   @Override
   public String modCurrent()
   {
      String outcome = super.modCurrent();
      this.githubContent = GithubImporter.getContent(getElement().getContent());
      return outcome;
   }
}
