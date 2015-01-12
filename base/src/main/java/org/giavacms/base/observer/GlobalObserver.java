/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.observer;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.giavacms.base.event.LanguageEvent;
import org.giavacms.base.repository.PageRepository;
import org.jboss.logging.Logger;

@Singleton
public class GlobalObserver implements Serializable
{

   @Inject
   PageRepository pageRepository;
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   public GlobalObserver()
   {
   }

   public void observe(@Observes LanguageEvent languageEvent)
   {
      pageRepository.updateLanguageByTemplate(languageEvent.getLang(), languageEvent.getTemplateImplId(),
               languageEvent.isSet());
   }

}
