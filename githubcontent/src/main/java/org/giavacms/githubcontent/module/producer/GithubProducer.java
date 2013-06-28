package org.giavacms.githubcontent.module.producer;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.producer.AbstractProducer;
import org.giavacms.githubcontent.model.GithubContentType;
import org.giavacms.githubcontent.repository.GithubContentTypeRepository;

@Named
@SessionScoped
public class GithubProducer extends AbstractProducer
{

   private static final long serialVersionUID = 1L;

   @Inject
   GithubContentTypeRepository githubTypeRepository;

   public GithubProducer()
   {
   }

   @Produces
   @Named
   public SelectItem[] getGithubTypeItems()
   {
      if (items.get(GithubContentType.class) == null)
      {
         List<GithubContentType> githubTypes = githubTypeRepository.getAllList();
         SelectItem[] githubTypeItems = new SelectItem[githubTypes.size()];
         for (int i = 0; i < githubTypeItems.length; i++)
         {
            githubTypeItems[i] = new SelectItem(githubTypes.get(i).getRichContentType().getId(), githubTypes.get(i)
                     .getRichContentType().getName());
         }
         items.put(GithubContentType.class, githubTypeItems);
      }
      return items.get(GithubContentType.class);
   }

}
