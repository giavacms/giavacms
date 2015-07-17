package org.giavacms.contest.service.rs;

import org.giavacms.api.annotation.AccountTokenVerification;
import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.model.pojo.Ranking;
import org.giavacms.contest.repository.VoteRepository;
import org.giavacms.contest.util.ServletContextUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.CONTEST_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VoteRepositoryRs extends RsRepositoryService<Vote>
{

   private static final long serialVersionUID = 1L;

   @Context
   HttpServletRequest httpServletRequest;

   public VoteRepositoryRs()
   {
   }

   @Inject
   public VoteRepositoryRs(VoteRepository repository)
   {
      super(repository);
   }

   @Override
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
      search.getNot().setActive(false);
      search.getObj().setCreated(new Date());
      search.getObj().setConfirmed(new Date());
      List<Vote> list = getRepository().getList(search, 0, 0);
      if (list != null && list.size() > 2)
      {
         throw new Exception(" - ER4 - puoi votare al massimo 3 volte al giorno.");
      }
      vote.setTocall(ServletContextUtils.getVoteNumber(servletContext));
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
            return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg",
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
         List<Vote> list = getRepository().getList(search, 0, 0);
         if (list != null && list.size() > 2)
         {
            return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "ER4 - puoi votare al massimo 3 volte al giorno.");

         }
         else
         {
            vote.setConfirmed(new Date());
            vote.setActive(true);
            vote = ((VoteRepository) getRepository()).persist(vote);
            return Response.status(Status.OK).entity(vote)
                     .build();
         }

      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error CREATING VOTE FOR " + vote.getPhone());
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
         Vote vote = ((VoteRepository) getRepository()).find(uuid);
         boolean isConfirmed = vote.getConfirmed() != null && vote.isActive();
         return jsonResponse(Status.OK, "msg", isConfirmed);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error reading confirmed for " + uuid);
      }
   }

   @GET
   @Path("/{preference}/rankings")
   @AccountTokenVerification
   public Response rankings(@PathParam("preference") String preference, @QueryParam("when") String when)
   {
      logger.info("@GET /" + preference + "/rankings");
      try
      {
         DateFormat df = new SimpleDateFormat("YYYYMMDDThhmmssZ");
         Date dateWhen = df.parse(when);
         List<Ranking> rankings = ((VoteRepository) getRepository()).getRanking(preference, dateWhen);
         // PaginatedListWrapper<T> wrapper = new PaginatedListWrapper<>();
         // wrapper.setList(list);
         // wrapper.setListSize(listSize);
         // wrapper.setStartRow(startRow);
         return Response.status(Status.OK).entity(rankings)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("{'msg' : 'Error reading ranking list for " + preference + "'}")
                  .type(MediaType.APPLICATION_JSON_TYPE).build();
      }
   }

   @GET
   @AccountTokenVerification
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getList(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @Context UriInfo ui)
   {
      return super.getList(startRow, pageSize, orderBy, ui);
   }

   @AccountTokenVerification
   @PUT
   @Path("/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response update(@PathParam("id") String id, Vote object) throws Exception
   {
      return super.update(id, object);
   }

}