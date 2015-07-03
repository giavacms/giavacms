package org.giavacms.chalet.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletRanking;
import org.giavacms.chalet.model.ChaletParade;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.chalet.repository.ChaletParadeRepository;
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
   ChaletParadeRepository paradeRepository;

   @Asynchronous
   public void create(Date atTime)
   {
      try
      {
         List<String> preferences = Arrays.asList("preference1");
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
         ChaletParade parade = new ChaletParade();
         parade.setData(atTime);
         parade.setName(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(atTime));
         parade.setPreference(preference);
         Map<String, Chalet> map_licenseNumber_chalet = chaletRepository.getChaletMap();
         int currentVotes = Integer.MAX_VALUE;
         int currentPosition = 0;
         for (Ranking ranking : rankings)
         {
            if (ranking.getVotes() < currentVotes)
            {
               currentVotes = ranking.getVotes();
               currentPosition++;
            }
            Chalet chalet = map_licenseNumber_chalet.get(ranking.getParticipationId());
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
