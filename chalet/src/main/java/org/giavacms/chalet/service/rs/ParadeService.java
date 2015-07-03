package org.giavacms.chalet.service.rs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.api.model.Search;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletRanking;
import org.giavacms.chalet.model.Parade;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.chalet.repository.ParadeRepository;
import org.giavacms.contest.model.pojo.Ranking;
import org.giavacms.contest.repository.VoteRepository;
import org.jboss.logging.Logger;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Stateless
public class ParadeService implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @Inject
   VoteRepository voteRepository;

   @Inject
   ChaletRepository chaletRepository;

   @Inject
   ParadeRepository paradeRepository;

   @Asynchronous
   public void create(Date atTime)
   {
      try
      {
         List<String> preferences = Arrays.asList("1");
         // preferences = SELECT DISTINCT PREFERENCES FROM VOTE....
         for (String preference : preferences)
         {
            create(preference, atTime);
         }
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
      }
   }

   private void create(String preference, Date atTime)
   {
      try
      {
         List<Ranking> rankings = voteRepository.getRanking(preference, atTime);
         Parade parade = new Parade();
         parade.setData(atTime);
         parade.setName(preference);
         List<Chalet> chalets = chaletRepository.getList(new Search<Chalet>(Chalet.class), 0, 0);
         Map<String, Chalet> map_idChalet_chalet = new HashMap<>();
         for (Chalet chalet : chalets)
         {
            map_idChalet_chalet.put(chalet.getId(), chalet);
         }
         int currentVotes = Integer.MAX_VALUE;
         int currentPosition = 0;
         for (Ranking ranking : rankings)
         {
            if (ranking.getVotes() < currentVotes)
            {
               currentVotes = ranking.getVotes();
               currentPosition++;
            }
            Chalet chalet = map_idChalet_chalet.get(ranking.getParticipationId());
            if (chalet == null)
            {
               continue;
            }
            ChaletRanking chaletRanking = new ChaletRanking(chalet.getName(), chalet.getLicenseNumber(),
                     ranking.getVotes(), currentPosition);
            parade.addChaletRanking(chaletRanking);
         }
         paradeRepository.persist(parade);
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
      }
   }
}
