package org.giavacms.githubcontent.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.githubcontent.util.GithubImporter;
import org.giavacms.richcontent.controller.request.RichContentRequestController;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.repository.RichContentRepository;

@Named
@RequestScoped
public class GithubContentRequestController extends
         RichContentRequestController
{

   private static final long serialVersionUID = 1L;

   private Object idValue;

   @Inject
   @OwnRepository(RichContentRepository.class)
   RichContentRepository richContentRepository;

   @Override
   public Object getIdValue()
   {
      if (this.idValue == null)
      {
         idValue = super.getIdValue();
      }
      return idValue;
   }

   public void setIdValue(Object idValue)
   {
      this.idValue = idValue;
   }

   @Override
   public RichContent getElement()
   {
      RichContent githubContent = super.getElement();
      githubContent.setContent(GithubImporter.getContent(githubContent.getContent()));
      return githubContent;
   }

   public String getDirectGithub(String url)
   {
      return GithubImporter.getContent(url);
   }
}
