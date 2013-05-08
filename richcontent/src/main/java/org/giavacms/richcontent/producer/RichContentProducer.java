package org.giavacms.richcontent.producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;
import org.jboss.logging.Logger;

@Named
@SessionScoped
public class RichContentProducer implements Serializable
{

   private static final long serialVersionUID = 1L;
   protected final Logger logger = Logger.getLogger(getClass()
            .getCanonicalName());

   @SuppressWarnings("rawtypes")
   private Map<Class, SelectItem[]> items = null;

   @Inject
   RichContentTypeRepository richContentTypeRepository;
   @Inject
   TagRepository tagRepository;
   private List<Group<Tag>> tags;

   // ==============================================================================
   public RichContentProducer()
   {

   }

   @SuppressWarnings("rawtypes")
   @PostConstruct
   public void reset()
   {
      logger.info("reset");
      items = new HashMap<Class, SelectItem[]>();
      tags = null;
   }

   @SuppressWarnings("rawtypes")
   public void resetItemsForClass(Class clazz)
   {
      if (items.containsKey(clazz))
      {
         items.remove(clazz);
      }
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

   @Produces
   @Named
   public List<Group<Tag>> getTags()
   {
      if (tags == null)
      {
         Search<Tag> st = new Search<Tag>(Tag.class);
         st.setGrouping("tagName");
         tags = tagRepository.getGroups(st, 0, 50);
      }
      return tags;
   }

}
