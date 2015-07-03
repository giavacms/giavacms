package org.giavacms.chalet.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletRanking;
import org.giavacms.chalet.model.Parade;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.chalet.repository.ParadeRepository;
import org.giavacms.contest.model.pojo.User;
import org.giavacms.contest.repository.VoteRepository;
import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fiorenzo on 03/07/15.
 */
public class SmsParadeServiceRs implements Serializable
{

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

   public Response sendSms()
   {
      List<ChaletRanking> chaletRankings = new ArrayList<>();
      try
      {
         Map<String, Chalet> chaletMap = chaletRepository.getChaletMap();
         List<Parade> parades = paradeRepository.getList(new Search<Parade>(Parade.class), 0, 1);
         Map<String, List<User>> mapUsers = voteRepository.getUsersForPreference();
         Parade parade = parades.get(0);
         for (ChaletRanking ranking : parade.getChaletRankings())
         {
            Chalet chalet = chaletMap.get(ranking.getLicenseNumber());
            List<User> users = mapUsers.get(chalet.getLicenseNumber());
            for (User user : users)
            {
               user.setPosition(ranking.getPosition());

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

   public void sendSmsToQueue(User user)
   {
      try
      {
         MapMessage mapMessage = context.createMapMessage();
         mapMessage.setString(AppKeys.USER_surname.name(), user.getSurname());

         context.createProducer().send(notificationQueue, mapMessage);
         logger.info("SENT - uid:");
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
         logger.error("NOT SENT - uid:");
      }
   }
}
