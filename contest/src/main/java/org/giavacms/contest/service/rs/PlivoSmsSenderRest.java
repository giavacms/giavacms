package org.giavacms.contest.service.rs;

import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.util.LoggerCallUtils;
import org.jboss.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Path(AppConstants.BASE_PATH + AppConstants.CONTEST_SMS_PATH)
@Stateless
@LocalBean
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlivoSmsSenderRest implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @POST
   @GET
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_XML)
   @Path("/url")
   public Response url(@Context HttpServletRequest httpServletRequest) throws Exception
   {
      logger.info("plivo sms url executing");
      LoggerCallUtils.log(httpServletRequest);
      org.plivo.ee.helper.xml.Response responseXml = new org.plivo.ee.helper.xml.Response();
      return Response
               .ok()
               .entity(responseXml).build();
   }

}
