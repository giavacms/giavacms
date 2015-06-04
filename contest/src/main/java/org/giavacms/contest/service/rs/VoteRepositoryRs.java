package org.giavacms.contest.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.contest.management.AppConstants;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.repository.VoteRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.CONTEST_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VoteRepositoryRs extends RsRepositoryService<Vote>
{

   private static final long serialVersionUID = 1L;

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
      logger.info("prePersist: " + vote);
      StringBuffer exceptionBuffr = new StringBuffer();
      if (vote.getName() == null || vote.getName().trim().isEmpty())
      {
         exceptionBuffr.append("il nome non puo' essere vuoto.");
      }
      if (vote.getSurname() == null || vote.getSurname().trim().isEmpty())
      {
         exceptionBuffr.append("il cognome non puo' essere vuoto.");
      }
      if (vote.getPhone() == null || vote.getPhone().trim().isEmpty())
      {
         exceptionBuffr.append("il numero di telefono non puo' essere vuoto.");
      }
      if (exceptionBuffr.length() > 0)
      {
         throw new Exception(exceptionBuffr.toString());
      }
      Search<Vote> search = new Search<Vote>(Vote.class);
      search.getObj().setName(vote.getName());
      search.getObj().setSurname(vote.getSurname());
      search.getObj().setPhone(vote.getPhone());
      search.getNot().setActive(false);
      List<Vote> list = getRepository().getList(search, 0, 0);
      if (list != null && list.size() > 0)
      {
         throw new Exception("esiste gia' un voto con stesso numero di telefono, nome, cognome.");
      }
      else
      {
         search = new Search<Vote>(Vote.class);
         search.getNot().setActive(false);
         search.getObj().setPhone(vote.getPhone());
         list = getRepository().getList(search, 0, 0);
         if (list != null && list.size() > 0)
         {
            throw new Exception("esiste gia' un voto con stesso numero di telefono.");
         }
      }
   }

}