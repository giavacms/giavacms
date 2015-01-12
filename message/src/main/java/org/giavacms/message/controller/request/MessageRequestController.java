package org.giavacms.message.controller.request;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.message.model.Message;
import org.giavacms.message.repository.MessageConfigurationRepository;
import org.giavacms.message.repository.MessageRepository;

@Named
@RequestScoped
public class MessageRequestController extends
         AbstractRequestController<Message> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("key")
   String key;

   @Inject
   @HttpParam("type")
   String type;

   @Inject
   @HttpParam("body")
   String body;

   @Inject
   @HttpParam("email")
   String email;

   @Inject
   @HttpParam("name")
   String name;

   public static final String ID_PARAM = "idMessage";
   public static final String CURRENT_PAGE_PARAM = "currentpage";

   @HttpParam(ID_PARAM)
   @Inject
   String idMessage;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String currentpage;
   @Inject
   @OwnRepository(MessageRepository.class)
   MessageRepository messageRepository;

   @Inject
   MessageConfigurationRepository messageConfigurationRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setSourceKey(key);
      getSearch().getObj().setSourceType(type);
      getSearch().getObj().setActive(true);
      getSearch().setOrder("date asc");
      super.initSearch();
   }

   @Override
   public String getIdParam()
   {
      return ID_PARAM;
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   public String getReturnMessage()
   {
      if (body != null && name != null)
      {
         Message message = new Message();
         message.setDate(new Date());
         message.setEmail(email);
         message.setName(name);
         message.setBody(body);
         message.setSourceKey(key);
         message.setSourceType(type);
         if (!messageConfigurationRepository.load().isApprove())
         {
            message.setActive(true);
         }
         messageRepository.persist(message);
         return "Grazie per il tuo commento!";
      }
      else
      {
         return null;
      }
   }

}
