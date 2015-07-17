package org.giavacms.contest.service.rs;

import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Account;
import org.giavacms.contest.model.pojo.User;
import org.giavacms.contest.repository.AccountRepository;
import org.giavacms.contest.repository.VoteRepository;
import org.giavacms.contest.util.LoggerCallUtils;
import org.jboss.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Path(AppConstants.BASE_PATH + AppConstants.CONTEST_VOTE_PATH)
@Stateless
@LocalBean
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlivoVoteRs implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   VoteRepository voteRepository;

   @Inject
   AccountRepository accountRepository;

   @POST
   @GET
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_XML)
   @Path("/answer")
   public Response answer(@Context HttpServletRequest httpServletRequest)
   {
      logger.info("plivo buzz answer executing");
      try
      {
         Map<String, String> map = LoggerCallUtils.log(httpServletRequest);
         String from = map.get(AppConstants.PLIVO_FROM);
         String to = map.get(AppConstants.PLIVO_TO);
         logger.info("RECEIVED CALL FROM: " + from + " - TO: " + to);
         int result = voteRepository.confirmVote(from);
         if (accountRepository.exist(from) == null)
         {
            Account account = new Account();
            account.setCreated(new Date());
            account.setPhone(from);
            account.setUserRoles("USER");
            User user;
            switch (result)
            {
            case 0:

               break;
            case 1:
               user = voteRepository.getUserByPhone(from.substring(2));
               account.setName(user.getName());
               account.setSurname(user.getSurname());
               accountRepository.persist(account);
               break;
            case 2:
               user = voteRepository.getUserByPhone(from);
               account.setName(user.getName());
               account.setSurname(user.getSurname());
               accountRepository.persist(account);
               break;
            }
         }
         else
         {
            logger.info("account exists already");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      org.plivo.ee.helper.xml.Response responseXml = new org.plivo.ee.helper.xml.Response();
      responseXml.hangUp();
      return Response
               .ok()
               .entity(responseXml).build();
   }

   @POST
   @GET
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_XML)
   @Path("/fallback")
   public Response fallback(@Context HttpServletRequest httpServletRequest)
   {
      logger.info("plivo buzz fallback executing");
      try
      {
         LoggerCallUtils.log(httpServletRequest);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      org.plivo.ee.helper.xml.Response responseXml = new org.plivo.ee.helper.xml.Response();
      return Response
               .ok()
               .entity(responseXml).build();
   }

   @POST
   @GET
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_XML)
   @Path("/hangup")
   public Response hangup(@Context HttpServletRequest httpServletRequest)
   {
      logger.info("plivo buzz hangup executing");
      try
      {
         LoggerCallUtils.log(httpServletRequest);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      org.plivo.ee.helper.xml.Response responseXml = new org.plivo.ee.helper.xml.Response();
      return Response
               .ok()
               .entity(responseXml).build();
   }

}
