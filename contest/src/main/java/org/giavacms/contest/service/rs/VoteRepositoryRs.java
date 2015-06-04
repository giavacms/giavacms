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
      Search<Vote> search = new Search<Vote>(Vote.class);
      search.getObj().setName(vote.getName());
      search.getObj().setSurname(vote.getSurname());
      search.getObj().setPhone(vote.getPhone());
      List<Vote> list = getRepository().getList(search, 0, 0);
      if (list != null && list.size() > 0)
      {
         throw new Exception("esiste gia' un voto con stesso numero, nome, cognome");
      }
   }

}