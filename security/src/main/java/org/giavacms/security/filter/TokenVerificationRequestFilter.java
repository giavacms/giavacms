package org.giavacms.security.filter;

import org.giavacms.api.annotation.AccountTokenVerification;
import org.giavacms.api.util.HttpUtils;
import org.giavacms.security.model.UserAuth;
import org.giavacms.security.repository.UserAuthRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Provider
@AccountTokenVerification
public class TokenVerificationRequestFilter implements ContainerRequestFilter
{

   @Inject
   UserAuthRepository userAuthRepository;

   private final static Logger logger = Logger.getLogger(TokenVerificationRequestFilter.class.getName());

   @Override
   public void filter(ContainerRequestContext requestCtx) throws IOException
   {
      logger.info("Executing REST TokenVerificationRequestFilter filter");
      try
      {
         String token = HttpUtils.getBearerToken(requestCtx.getHeaders());
         if (token == null || token.trim().isEmpty())
         {
            logger.error("token empty");
            requestCtx.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
            return;
         }
         UserAuth account = userAuthRepository.findByToken(token);
         if (account == null)
         {
            logger.error("no user found by token");
            requestCtx.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
            return;
         }
         String userid = null;
         for (String key : requestCtx.getUriInfo().getPathParameters().keySet())
         {
            if (key.equals("userid"))
            {
               List<String> value = requestCtx.getUriInfo().getPathParameters().get(key);
               userid = value.get(0);
               break;
            }
         }
         if (userid == null || userid.isEmpty())
         {
            logger.error("userid path parameter null or empty");
            requestCtx.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
            return;
         }

         if (!account.getUsername().equals(userid))
         {
            logger.error("userid params does not match token's userid");
            requestCtx.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
            return;
         }
      }
      catch (Exception e)
      {
         requestCtx.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
      }
   }
}
