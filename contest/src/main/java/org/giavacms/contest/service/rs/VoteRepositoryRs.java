package org.giavacms.contest.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;
import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.repository.VoteRepository;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;

@Path(AppConstants.BASE_PATH + AppConstants.VOTES_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class VoteRepositoryRs extends RsRepositoryService<Vote>
{

   private static final long serialVersionUID = 471883957307995404L;

   @Resource
   SessionContext sessionContext;

   public VoteRepositoryRs()
   {
   }

   @Inject
   public VoteRepositoryRs(VoteRepository repository)
   {
      super(repository);
   }

   @Override
   public Response delete(String id) throws Exception
   {
      return super
               .jsonResponse(Response.Status.FORBIDDEN, AppConstants.RS_MSG, "forbidden");
   }

   @Override
   public Response persist(Vote object) throws Exception
   {
      return super
               .jsonResponse(Response.Status.FORBIDDEN, AppConstants.RS_MSG, "forbidden");
   }

   @Override
   public Response update(String id, Vote object) throws Exception
   {
      return super
               .jsonResponse(Response.Status.FORBIDDEN, AppConstants.RS_MSG, "forbidden");
   }

   @Override
   @GET
   @AccountTokenVerification
   public Response getList(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @Context UriInfo ui)
   {
      Principal callerPrincipal = sessionContext.getCallerPrincipal();
      String phone = (String) callerPrincipal.getName();
      logger.info("phone: " + phone);
      Search<Vote> search = getSearch(ui, orderBy);
      if (search.getObj() == null || !search.getObj().getPhone().equals(phone))
      {
         return super
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, AppConstants.RS_MSG, "no username in request");
      }
      return super.getList(startRow, pageSize, orderBy, ui);
   }
}