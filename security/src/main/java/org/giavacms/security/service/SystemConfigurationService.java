/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.service;

import org.giavacms.security.repository.UserAuthRepository;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;

@Singleton
@Startup
public class SystemConfigurationService implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected final Logger logger = Logger.getLogger(getClass()
            .getCanonicalName());

   @Inject
   UserAuthRepository userAuthRepository;

   public SystemConfigurationService()
   {
      logger.info("SystemConfigurationService: start!");
   }

   @PostConstruct
   public void verifyConfiguration()
   {
      logger.info("init system: verify if admin exist");
      userAuthRepository.verifyConfiguration();
   }
}
