package org.giavacms.contest.service;

import org.giavacms.contest.repository.TokenRepository;
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
public class TokenTimer implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   TokenRepository tokenRepository;

   @Lock(LockType.READ)
   @Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
   public void timeout()
   {
      logger.info("TIMEOUT TO DELETE TOKEN NOT CONFIRMED");
      tokenRepository.deleteNotConfirmed();
   }
}
