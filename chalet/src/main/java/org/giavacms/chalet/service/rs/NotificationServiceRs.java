package org.giavacms.chalet.service.rs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.giavacms.api.model.Search;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletRanking;
import org.giavacms.chalet.model.Parade;
import org.giavacms.chalet.model.enums.SmsTypes;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.chalet.repository.ParadeRepository;
import org.giavacms.chalet.utils.MsgUtils;
import org.giavacms.contest.model.pojo.User;
import org.giavacms.contest.repository.VoteRepository;
import org.jboss.logging.Logger;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Path(AppConstants.BASE_PATH + AppConstants.NOTIFICATION_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationServiceRs implements Serializable
{

   private static final long serialVersionUID = 1L;
   protected final Logger logger = Logger.getLogger(getClass());
   @Inject
   VoteRepository voteRepository;

   @Inject
   ParadeRepository paradeRepository;

   @Inject
   ChaletRepository chaletRepository;

   @Inject
   @JMSConnectionFactory("java:/ConnectionFactory")
   private JMSContext context;

   @Resource(lookup = AppConstants.QUEUE_NOTIFICATION_SENDER)
   private Queue notificationQueue;

   public Response sendParadeSms()
   {
      List<ChaletRanking> chaletRankings = new ArrayList<>();
      try
      {
         String preference = null;
         Parade parade = paradeRepository.getLast(preference);
         if (parade == null)
         {
            return Response.status(Response.Status.NOT_FOUND)
                     .entity("No parade found").build();
         }
         Map<String, Chalet> chaletMap = chaletRepository.getChaletMap();
         Map<String, List<User>> mapUsers = voteRepository.getUsersForPreference();
         for (ChaletRanking ranking : parade.getChaletRankings())
         {
            Chalet chalet = chaletMap.get(ranking.getLicenseNumber());
            List<User> users = mapUsers.get(chalet.getLicenseNumber());
            for (User user : users)
            {
               user.setPosition(ranking.getPosition());
               sendSmsToQueue(MsgUtils.paradeSms(user.getName() + " " + user.getSurname(), chalet.getName(),
                        chalet.getLicenseNumber(),
                        "" + ranking.getPosition()), user.getPhone());
            }
         }
      }
      catch (Exception e)
      {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity("Error ranking: " + e.getMessage()).build();
      }
      return Response.status(Response.Status.OK).entity(chaletRankings).build();
   }

   public void sendSmsToQueue(String message, String number)
   {
      try
      {
         MapMessage mapMessage = context.createMapMessage();
         mapMessage.setString(AppKeys.MESSAGE_number.name(), number);
         mapMessage.setString(AppKeys.MESSAGE_text.name(), message);
         mapMessage.setString(AppKeys.MESSAGE_type.name(), SmsTypes.PARADE.name());

         context.createProducer().send(notificationQueue, mapMessage);
         logger.info("SMS PARADE - to: " + number + " msg: " + message);
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
         logger.error("NOT SENT - uid:");
      }
   }
}
