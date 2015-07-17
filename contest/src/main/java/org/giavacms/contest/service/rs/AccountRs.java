package org.giavacms.contest.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.util.JWTUtils;
import org.giavacms.contest.model.Account;
import org.giavacms.contest.model.Token;
import org.giavacms.contest.repository.AccountRepository;
import org.giavacms.contest.repository.TokenRepository;
import org.giavacms.contest.util.ServletContextUtils;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by fiorenzo on 17/07/15.
 */
@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountRs implements Serializable
{
   private static final long serialVersionUID = 6387740854416008191L;

   @Inject
   AccountRepository accountRepository;

   @Inject
   TokenRepository tokenRepository;

   @Context
   HttpServletRequest httpServletRequest;

   protected final Logger logger = Logger.getLogger(getClass());

   @POST
   public Response create(Account account)
   {

      return RsRepositoryService
               .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid number ");
   }

   @POST
   @Path("/login")
   public Response login(String phone)
   {
      ServletContext servletContext = httpServletRequest.getServletContext();
      logger.info("@POST /login: " + phone);
      if (phone == null || phone.trim().isEmpty())
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid number ");
      }
      //verificare che esiste
      Account account = accountRepository.exist(phone);
      if (account != null)
      {
         Token token = new Token(new Date(), phone, account.getName() + " " + account.getSurname(),
                  account.getUserRoles());
         try
         {
            token = tokenRepository.persist(token);
            //se esiste ti torno un numero di telefono da chiamare
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("tocall", ServletContextUtils.getTokenNumber(servletContext));
            jsonObjBuilder.add("uuid", token.getUuid());
            JsonObject jsonObj = jsonObjBuilder.build();
            return Response.status(Response.Status.OK)
                     .entity(jsonObj.toString())
                     .build();
         }
         catch (Exception e)
         {
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "Error in create token ");
         }

      }
      else
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid number ");
      }
   }

   @POST
   @Path("/logout")
   public Response logout(String phone)
   {
      logger.info("@POST /logout: " + phone);
      if (phone == null || phone.trim().isEmpty())
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid number ");
      }
      //verificare che esiste
      Account account = accountRepository.exist(phone);
      if (account != null)
      {
         Token token = tokenRepository.exist(phone);
         try
         {
            token.setDestroyed(new Date());
            token = tokenRepository.update(token);
            return RsRepositoryService
                     .jsonResponse(Response.Status.NO_CONTENT, "msg", "logout for: " + phone);
         }
         catch (Exception e)
         {
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "Error in create token ");
         }

      }
      return RsRepositoryService.jsonResponse(Response.Status.BAD_REQUEST, "msg",
               "No valid number: " + phone);
   }

   @GET
   @Path("/login/{uuid}/token")
   public Response confirmed(@PathParam("uuid") String uuid)
   {
      ServletContext servletContext = httpServletRequest.getServletContext();
      logger.info("@GET /" + uuid + "/token");
      try
      {
         Token token = tokenRepository.find(uuid);
         if (token.getCreated() != null && token.getConfirmed() != null && token.getDestroyed() == null
                  && token.getToken() == null)
         {
            String tokenString = JWTUtils
                     .encode(ServletContextUtils.getJwtSecret(servletContext), ServletContextUtils.getJwtExpireTime(
                                       servletContext), token.getPhone(),
                              token.getName(), Arrays.asList(token.getUserRoles().split(",|;")));
            token.setConfirmed(new Date());
            token.setDuration(ServletContextUtils.getJwtExpireTime(servletContext));
            token.setToken(tokenString);
            tokenRepository.update(token);
            return RsRepositoryService
                     .jsonResponse(Response.Status.OK, "token", tokenString);
         }
         else
         {
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "no token valid for " + uuid);
         }

      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", " error in confirm " + uuid);
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
