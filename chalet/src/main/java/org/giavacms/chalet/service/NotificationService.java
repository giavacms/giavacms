package org.giavacms.chalet.service;

import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletParade;
import org.giavacms.chalet.model.ChaletRanking;
import org.giavacms.chalet.model.FreeTicket;
import org.giavacms.chalet.model.enums.SmsTypes;
import org.giavacms.chalet.repository.ChaletParadeRepository;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.chalet.repository.FreeTicketRepository;
import org.giavacms.chalet.repository.FreeTicketWinnerRepository;
import org.giavacms.chalet.utils.MsgUtils;
import org.giavacms.contest.model.pojo.User;
import org.giavacms.contest.repository.VoteRepository;
import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MapMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Stateless
public class NotificationService
{

   private static final long serialVersionUID = 1L;
   protected final Logger logger = Logger.getLogger(getClass());
   @Inject
   VoteRepository voteRepository;

   @Inject
   ChaletParadeRepository paradeRepository;

   @Inject
   FreeTicketWinnerRepository freeTicketWinnerRepository;

   @Inject
   FreeTicketRepository freeTicketRepository;

   @Inject
   ChaletRepository chaletRepository;

   @Inject
   @JMSConnectionFactory("java:/ConnectionFactory")
   private JMSContext context;

   @Resource(lookup = AppConstants.QUEUE_NOTIFICATION_SENDER)
   private javax.jms.Queue notificationQueue;

   public List<ChaletRanking> sendParadeSms() throws Exception
   {
      List<ChaletRanking> chaletRankings = new ArrayList<>();
      try
      {
         String preference = null;
         ChaletParade parade = paradeRepository.getLast(preference);
         if (parade == null)
         {
            throw new Exception("No parade found");
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
         throw new Exception("Error ranking: " + e.getMessage());
      }
      return chaletRankings;
   }

   public boolean sendTickets() throws Exception
   {
      //prendo la lista dei numeri gia vincitori
      List<String> alreadyWinners = freeTicketWinnerRepository.getAllreadyWinners();

      Map<String, List<FreeTicket>> freeTicketsForChalet = null;
      try
      {
         freeTicketsForChalet = freeTicketRepository.getFreeTicketForChalet(new Date());
         // cerco ticket per questo week end
         for (String licenseNumber : freeTicketsForChalet.keySet())
         {
            List<FreeTicket> tickets = freeTicketsForChalet.get(licenseNumber);
            if (tickets == null || tickets.isEmpty())
            {
               throw new Exception("NO FREE TICKETS FRO TODAY");
            }
            int numOfTickets = tickets.size();
            List<User> newWinners = voteRepository.getWinner(numOfTickets, licenseNumber, alreadyWinners);
            if (newWinners == null || newWinners.isEmpty())
            {
               List<String> io = new ArrayList<>();
               io.add("3922274929");
               newWinners = voteRepository.getWinner(numOfTickets, licenseNumber, io);
            }
            if (newWinners == null || newWinners.isEmpty())
            {
               throw new Exception("NO WINNERS TODAY");
            }

            sendSms(tickets, newWinners);
         }
      }
      catch (Exception e)
      {
         throw new Exception("Error sending sms fro free tickets: " + e.getMessage());
      }
      return true;
   }

   private void sendSms(List<FreeTicket> tickets, List<User> newWinners)
   {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, 5);
      DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      int i = 0;
      for (User user : newWinners)
      {
         FreeTicket freeTicket = tickets.get(i);
         //String fullName, String chaletName, String licenseNumber, String ticketName,  String expireDate)
         sendSmsToQueue(MsgUtils.ticketSms(user.getName() + " " + user.getSurname(), freeTicket.getChalet().getName(),
                  freeTicket.getChalet().getLicenseNumber(),
                  freeTicket.getDescription(), dateFormat.format(calendar.getTime())), user.getPhone());
         i++;
      }
   }

   private void sendSmsToQueue(String message, String number)
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
