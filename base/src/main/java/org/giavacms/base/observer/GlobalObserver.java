package org.giavacms.base.observer;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.event.LanguageEvent;
import org.giavacms.base.repository.PageRepository;
import org.jboss.logging.Logger;

@Named
@SessionScoped
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
      pageRepository.updateLanguagePage(languageEvent.getLang(), languageEvent.getTemplateImplId(),
               languageEvent.isSet());
   }

}
