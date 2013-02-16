package org.giavacms.message.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.message.model.Message;
import org.giavacms.message.repository.MessageRepository;
import org.jboss.logging.Logger;


@SessionScoped
@Named
public class MessageProducer implements Serializable
{

   protected final Logger logger = Logger.getLogger(getClass()
            .getCanonicalName());
   private static final long serialVersionUID = 1L;

   @Inject
   private MessageRepository messageRepository;

   @SuppressWarnings("rawtypes")
   private Map<Class, SelectItem[]> items = null;

   public MessageProducer()
   {
      // TODO Auto-generated constructor stub
   }

   @Produces
   @Named
   public SelectItem[] getSourceTyeItems()
   {
      if (items.get(Message.class) == null)
      {
         List<String> type = messageRepository.getDistinctType();
         List<SelectItem> listItems = new ArrayList<SelectItem>();
         for (String string : type)
         {
            listItems.add(new SelectItem(string));
         }
         items.put(Message.class, listItems.toArray(new SelectItem[] {}));
      }
      return items.get(Message.class);
   }

   public void resetItemsForClass(Class clazz)
   {
      if (items.containsKey(clazz))
      {
         items.remove(clazz);
      }
   }

   // ==============================================================================

   @PostConstruct
   public void reset()
   {
      items = new HashMap<Class, SelectItem[]>();
   }

}
