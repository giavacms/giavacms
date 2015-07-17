package org.giavacms.contest.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.repository.VoteRepository;
import org.giavacms.contest.util.LoggerCallUtils;
import org.jboss.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fiorenzo on 07/07/15.
 */
@Path(AppConstants.BASE_PATH + AppConstants.SKEBBY_SMS_PHONE_PATH)
@Stateless
@LocalBean
public class SkebbySmsReceiver implements Serializable
{
   @Inject
   VoteRepository voteRepository;

   /*
 * sender   SMS Sender's Numnber in international format without "+" or "00", Example: 393334455666
 * receiver The number where the SMS has arrived in international format without "+" or "00", Example: 393334455666
 * text     SMS text
 * encoding The character encoding scheme used for the text (ISO-8859-1)
 * date     SMS delivery date
 * time     SMS delivery time
 * timestamp    SMS delivery timestamp. For programmer's convenience, we pass 3 different date/time formats
 * smsType  type of the Received SMS (standard o skebby)
 */
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @POST
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public Response url(@Context HttpServletRequest httpServletRequest) throws Exception
   {
      logger.info("plivo sms url executing");
      Map<String, String> params = LoggerCallUtils.log(httpServletRequest);
      Search<Vote> search = new Search<Vote>(Vote.class);
      search.getNot().setActive(false);
      search.getObj().setPhone(params.get("sender"));
      List<Vote> list = voteRepository.getList(search, 0, 0);
      if (list != null && list.size() > 0)
      {
         logger.info("ER5 - esiste gia' un voto con stesso numero di telefono.");
      }
      else
      {
         Vote vote = new Vote(params.get("sender"));
         vote.setPreference1(params.get("text"));
         vote.setActive(true);
         vote.setName("Anonymous");
         vote.setSurname("Anonymous");
         vote.setCreated(new Date());
         vote.setConfirmed(new Date());
         voteRepository.persist(vote);
      }
      return Response
               .ok()
               .entity("ok").build();
   }

}
