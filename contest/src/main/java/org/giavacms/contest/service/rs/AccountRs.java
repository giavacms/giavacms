package org.giavacms.contest.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.util.JWTUtils;
import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Account;
import org.giavacms.contest.model.Token;
import org.giavacms.contest.model.pojo.Login;
import org.giavacms.contest.repository.AccountRepository;
import org.giavacms.contest.repository.TokenRepository;
import org.giavacms.contest.util.ServletContextUtils;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
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
@Path(AppConstants.BASE_PATH + AppConstants.ACCOUNTS_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
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

   public AccountRs()
   {
   }

   @POST
   public Response create(Account account)
   {
      ServletContext servletContext = httpServletRequest.getServletContext();
      logger.info("@POST /creatAccounte: " + account);
      StringBuffer exceptionBuffr = new StringBuffer();
      if (account == null)
      {
         exceptionBuffr.append(" - ER6 - account nullo.");
      }
      if (account.getName() == null || account.getName().trim().isEmpty())
      {
         exceptionBuffr.append(" - ER7 - il nome non puo' essere vuoto.");
      }
      if (account.getSurname() == null || account.getSurname().trim().isEmpty())
      {
         exceptionBuffr.append(" - ER7 - il cognome non puo' essere vuoto.");
      }
      if (account.getPhone() == null || account.getPhone().trim().isEmpty())
      {
         exceptionBuffr.append(" - ER8 - il telefono non puo' essere vuoto.");
      }
      if (exceptionBuffr.length() > 0)
      {
         logger.info(exceptionBuffr.toString());
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", exceptionBuffr.toString());
      }
      //verificare che esiste
      Account verify = accountRepository.exist(account.getPhone());
      if (verify != null)
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "Account already exist ");
      }
      else
      {
         try
         {
            logger.info("generateToken - TokenNumber: " + ServletContextUtils.getTokenNumber(servletContext));
            account.setTocall(ServletContextUtils.getTokenNumber(servletContext));
            account.setCreated(new Date());
            account.setConfirmed(null);
            account.setUserRoles("USER");
            account = accountRepository.persist(account);
            return generateToken(account, servletContext);
         }
         catch (Exception e)
         {
            e.printStackTrace();
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "generic error");
         }
      }
   }

   private Response generateToken(Account account, ServletContext servletContext)
   {
      logger.info("generateToken - account: " + account);
      if (account != null)
      {

         try
         {
            logger.info("generateToken - TokenNumber: " + ServletContextUtils.getTokenNumber(servletContext));
            Token token = tokenRepository
                     .persist(new Token(new Date(), account.getPhone(), account.getName() + " " + account.getSurname(),
                              account.getUserRoles()));
            logger.info(token);
            //se esiste ti torno un numero di telefono da chiamare
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("tocall", ServletContextUtils.getTokenNumber(servletContext));
            jsonObjBuilder.add("uuid", token.getUuid());
            JsonObject jsonObj = jsonObjBuilder.build();
            return Response.status(Response.Status.OK)
                     .entity(jsonObj.toString())
                     .build();
         }
         catch (Throwable e)
         {
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "Error in create token ");
         }

      }
      else
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid account");
      }
   }

   @POST
   @Path("/login")
   public Response login(Login login)
   {
      ServletContext servletContext = httpServletRequest.getServletContext();
      logger.info("@POST /login: " + login.getPhone());
      if (login == null || login.getPhone() == null || login.getPhone().trim().isEmpty())
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid number");
      }
      //verificare che esiste
      Account account = accountRepository.exist(login.getPhone());
      return generateToken(account, servletContext);
   }

   @POST
   @Path("/logout")
   public Response logout(Login login)
   {
      logger.info("@POST /logout: " + login.getPhone());
      if (login == null || login.getPhone() == null || login.getPhone().trim().isEmpty())
      {
         return RsRepositoryService
                  .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "No valid number ");
      }
      //verificare che esiste
      Account account = accountRepository.exist(login.getPhone());
      if (account != null)
      {
         Token token = tokenRepository.exist(login.getPhone());
         try
         {
            token.setDestroyed(new Date());
            token = tokenRepository.update(token);
            return RsRepositoryService
                     .jsonResponse(Response.Status.NO_CONTENT, "msg", "logout for: " + login.getPhone());
         }
         catch (Exception e)
         {
            return RsRepositoryService
                     .jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg", "Error in create token ");
         }

      }
      return RsRepositoryService.jsonResponse(Response.Status.BAD_REQUEST, "msg",
               "No valid number: " + login.getPhone());
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
