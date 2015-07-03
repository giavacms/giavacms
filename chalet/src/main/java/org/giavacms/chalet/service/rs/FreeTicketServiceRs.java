package org.giavacms.chalet.service.rs;

import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.contest.repository.VoteRepository;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

@Path(AppConstants.BASE_PATH + AppConstants.PARADE_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FreeTicketServiceRs implements Serializable
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

}
