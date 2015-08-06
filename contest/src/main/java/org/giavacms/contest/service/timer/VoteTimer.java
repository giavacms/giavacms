package org.giavacms.contest.service.timer;

import org.giavacms.contest.repository.VoteRepository;
import org.jboss.logging.Logger;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by fiorenzo on 04/06/15.
 */
@Singleton
public class VoteTimer implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   VoteRepository voteRepository;

   @Lock(LockType.READ)
   @Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
   public void timeout()
   {
      logger.info("TIMEOUT TO PASSIVATE VOTES NOT CONFIRMED");
      voteRepository.passivateOldVotes();
   }
}
