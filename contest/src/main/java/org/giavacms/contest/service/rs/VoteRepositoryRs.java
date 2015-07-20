package org.giavacms.contest.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.api.util.RepositoryUtils;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;
import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Account;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.repository.AccountRepository;
import org.giavacms.contest.repository.VoteRepository;
import org.giavacms.contest.util.ServletContextUtils;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.CONTEST_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VoteRepositoryRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @Inject
   AccountRepository accountRepository;

   @Inject
   VoteRepository repository;

   @Context
   HttpServletRequest httpServletRequest;

   public VoteRepositoryRs()
   {
   }

   @POST
   public Response persist(Vote object) throws Exception
   {
      logger.info("@POST / persist ");
      try
      {
         prePersist(object);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
         return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                  "Error before creating resource: " + e.getMessage());
      }
      try
      {
         Vote persisted = repository.persist(object);
         if (persisted == null || RepositoryUtils.getId(persisted) == null)
         {
            logger.error("Failed to create resource: " + object);
            return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR,
                     "msg", "Failed to create resource: " + object.toString());
         }
         else
         {
            return Response.status(Status.OK).entity(persisted)
                     .build();
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                  "ER6 - Error creating resource: " + object.toString());
      }
   }

   protected void prePersist(Vote vote) throws Exception
   {
      ServletContext servletContext = httpServletRequest.getServletContext();
      logger.info("prePersist: " + vote);

      StringBuffer exceptionBuffr = new StringBuffer();
      if (vote.getName() == null || vote.getName().trim().isEmpty())
      {
         exceptionBuffr.append(" - ER1 - il nome non puo' essere vuoto.");
      }
      if (vote.getSurname() == null || vote.getSurname().trim().isEmpty())
      {
         exceptionBuffr.append(" - ER2 - il cognome non puo' essere vuoto.");
      }
      if (vote.getPhone() == null || vote.getPhone().trim().isEmpty())
      {
         exceptionBuffr.append(" - ER3 - il numero di telefono non puo' essere vuoto.");
      }
      if (exceptionBuffr.length() > 0)
      {
         throw new Exception(exceptionBuffr.toString());
      }
      String phone = vote.getPhone().replace(" ", "").replace(".", "").replace("+", "").replace("/", "")
               .replace("\\", "");
      vote.setPhone(phone);
      Search<Vote> search = new Search<Vote>(Vote.class);
      search.getObj().setPhone(vote.getPhone());
      search.getNot().setActive(true);
      search.getObj().setCreated(new Date());
      search.getObj().setConfirmed(new Date());
      List<Vote> list = repository.getList(search, 0, 0);
      if (list != null && list.size() > 2)
      {
         throw new Exception(" - ER4 - puoi votare al massimo 3 volte al giorno.");
      }
      vote.setTocall(ServletContextUtils.getVoteNumber(servletContext));
      vote.setActive(true);
   }

   @POST
   @Path("/reVote")
   public Response reVote(Vote vote)
   {
      logger.info("@POST / reVote ");
      ServletContext servletContext = httpServletRequest.getServletContext();
      StringBuffer exceptionBuffr = new StringBuffer();
      try
      {
         if (vote.getPhone() == null || vote.getPhone().trim().isEmpty())
         {
            logger.error("@POST / reVote - il numero di telefono non puo' essere vuoto.");
            return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                     "ER3 - il numero di telefono non puo' essere vuoto.");
         }
         Account account = accountRepository.exist(vote.getPhone());
         if (account == null)
         {
            logger.error("@POST / reVote - Account not existent: " + vote.getPhone());
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "ER5 - Account not existent ");
         }
         String phone = vote.getPhone().replace(" ", "").replace(".", "").replace("+", "").replace("/", "")
                  .replace("\\", "");
         vote.setPhone(phone);
         Search<Vote> search = new Search<Vote>(Vote.class);
         search.getObj().setPhone(vote.getPhone());
         search.getNot().setActive(false);
         search.getObj().setCreated(new Date());
         search.getObj().setConfirmed(new Date());
         List<Vote> list = repository.getList(search, 0, 0);
         if (list != null && list.size() > 2)
         {
            logger.error("@POST / reVote - puoi votare al massimo 3 volte al giorno.: " + vote.getPhone());
            return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                     "ER4 - puoi votare al massimo 3 volte al giorno.");

         }
         else
         {
            vote.setName(account.getName());
            vote.setSurname(account.getSurname());
            vote.setActive(true);
            vote.setTocall(ServletContextUtils.getVoteNumber(servletContext));
            vote = repository.persist(vote);
            return Response.status(Status.OK).entity(vote)
                     .build();
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                  "ER6 - Error CREATING VOTE FOR " + vote.getPhone());
      }
   }

   @POST
   @Path("/withToken")
   @AccountTokenVerification
   public Response createWithToken(Vote vote)
   {
      logger.info("@POST / withToken ");
      StringBuffer exceptionBuffr = new StringBuffer();
      try
      {
         if (vote.getPhone() == null || vote.getPhone().trim().isEmpty())
         {
            return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                     "ER3 - il numero di telefono non puo' essere vuoto.");
         }
         String phone = vote.getPhone().replace(" ", "").replace(".", "").replace("+", "").replace("/", "")
                  .replace("\\", "");
         vote.setPhone(phone);
         Search<Vote> search = new Search<Vote>(Vote.class);
         search.getObj().setPhone(vote.getPhone());
         search.getNot().setActive(false);
         search.getObj().setCreated(new Date());
         search.getObj().setConfirmed(new Date());
         List<Vote> list = repository.getList(search, 0, 0);
         if (list != null && list.size() > 2)
         {
            return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                     "ER4 - puoi votare al massimo 3 volte al giorno.");

         }
         else
         {
            vote.setConfirmed(new Date());
            vote.setActive(true);
            vote = repository.persist(vote);
            return Response.status(Status.OK).entity(vote)
                     .build();
         }

      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                  "ER6 - Error CREATING VOTE FOR " + vote.getPhone());
      }
   }

   @GET
   @Path("/{uuid}/confirmed")
   public Response confirmed(@PathParam("uuid") String uuid)
   {
      logger.info("@GET /" + uuid + "/confirmed");
      try
      {
         //         boolean isConfirmed = ((VoteRepository) getRepository()).isConfirmed(phone);
         Vote vote = repository.find(uuid);
         boolean isConfirmed = vote.getConfirmed() != null && vote.isActive();
         return RsRepositoryService.jsonResponse(Status.OK, "msg", isConfirmed);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
         return RsRepositoryService.jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
                  "ER6 - Error reading confirmed for " + uuid);
      }
   }

   @OPTIONS
   public Response options()
   {
      logger.info("@OPTIONS");
      return Response.ok().build();
   }

   @OPTIONS
   @Path("{path:.*}")
   public Response allOptions()
   {
      logger.info("@OPTIONS ALL");
      return Response.ok().build();
   }

}