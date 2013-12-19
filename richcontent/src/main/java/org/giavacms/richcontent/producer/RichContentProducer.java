package org.giavacms.richcontent.producer;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.common.producer.AbstractProducer;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentTypeRepository;

@Named
@SessionScoped
public class RichContentProducer extends AbstractProducer implements Serializable
{

   private static final long serialVersionUID = 1L;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   
   // @Inject
   // TagRepository tagRepository;

   // ==============================================================================

   public RichContentProducer()
   {
   }

   @Produces
   @Named
   public SelectItem[] getRichContentTypeItems()
   {
      if (items.get(RichContentType.class) == null)
      {
         items.put(RichContentType.class, JSFUtils.setupItems(
                  new Search<RichContentType>(RichContentType.class),
                  richContentTypeRepository, "id", "name", "nessuna categoria",
                  "seleziona categoria contenuto..."));
      }
      return items.get(RichContentType.class);
   }

}
