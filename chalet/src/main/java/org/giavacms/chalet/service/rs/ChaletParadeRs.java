package org.giavacms.chalet.service.rs;

import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.pojo.ChaletRanking;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.contest.model.pojo.Ranking;
import org.giavacms.contest.model.pojo.User;
import org.giavacms.contest.repository.VoteRepository;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Path(AppConstants.BASE_PATH + AppConstants.PARADE_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChaletParadeRs implements Serializable
{

   private static final long serialVersionUID = 471883957307995404L;

   @Inject
   VoteRepository voteRepository;

   @Inject
   ChaletRepository chaletRepository;

   @Inject
   @JMSConnectionFactory("java:/ConnectionFactory")
   private JMSContext context;

   @Resource(lookup = AppConstants.QUEUE_NOTIFICATION_SENDER)
   private Queue notificationQueue;

   public Response getChaletRanking()
   {
      List<ChaletRanking> chaletRankings = new ArrayList<>();
      try
      {
         Map<String, Chalet> chaletMap = chaletRepository.getChaletMap();
         List<Ranking> ranks = voteRepository.getRanking("preference1", new Date());
         for (Ranking ranking : ranks)
         {
            Chalet chalet = chaletMap.get(ranking.getParticipationId());
            chaletRankings.add(new ChaletRanking(chalet.getName(), chalet.getLicenseNumber(),
                     ranking.getVotes()));
         }
      }
      catch (Exception e)
      {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity("Error ranking: " + e.getMessage()).build();
      }
      return Response.status(Response.Status.OK).entity(chaletRankings).build();
   }

   public Response sendSms()
   {
      List<ChaletRanking> chaletRankings = new ArrayList<>();
      try
      {
         Map<String, Chalet> chaletMap = chaletRepository.getChaletMap();
         List<Ranking> ranks = voteRepository.getRanking("preference1", new Date());
         Map<String, List<User>> mapUsers = voteRepository.getUsersForPreference();
         for (Ranking ranking : ranks)
         {
            Chalet chalet = chaletMap.get(ranking.getParticipationId());
            List<User> users = mapUsers.get(chalet.getLicenseNumber());
            for (User user : users)
            {
               user.setPosition(ranking.getVotes());

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

   public void sendMsgToSplitPdf(String uuid, String fileName)
   {
      try
      {
         MapMessage mapMessage = context.createMapMessage();
         mapMessage.setString(AppKeys.UUID.name(), uuid);
         mapMessage.setString(AppKeys.FILE_NAME.name(), fileName);
         context.createProducer().send(pdfQueue, mapMessage);
         logger.info("SENT - uid:" + uuid + ", filename:" +
                  fileName + ", queue: " + AppConstants.QUEUE_PDF_STRIPPER);
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
         logger.error("NOT SENT - uid:" + uuid + ", filename:" +
                  fileName + ", queue: " + AppConstants.QUEUE_PDF_STRIPPER);
      }
   }

}
