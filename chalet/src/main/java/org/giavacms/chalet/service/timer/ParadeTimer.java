package org.giavacms.chalet.service.timer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.giavacms.chalet.service.rs.ParadeService;
import org.giavacms.contest.model.pojo.Ranking;
import org.giavacms.contest.repository.VoteRepository;
import org.jboss.logging.Logger;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Singleton
public class ParadeTimer
         implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   ParadeService paradeService;

   @Lock(LockType.READ)
   @Schedule(second = "0", minute = "0", hour = "*", persistent = false)
   public void timeout()
   {
      logger.info("TIMEOUT TO UPDATE THE CHALET PARADE");
      Date atTime = new Date();
      paradeService.create(atTime);
   }
}