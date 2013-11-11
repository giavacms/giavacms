package org.giavacms.cache.observer;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.giavacms.base.event.PageEvent;
import org.giavacms.base.event.TemplateEvent;
import org.giavacms.rewriter.service.CacheService;
import org.jboss.logging.Logger;

@Singleton
public class CacheObserver implements Serializable
{

   @Inject
   CacheService cacheService;
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   public CacheObserver()
   {
   }

   public void observe(@Observes PageEvent pageEvent)
   {
      cacheService.cacheByPageIdAndTemplateId(pageEvent.getPage().getId(), pageEvent.getPage().isClone(), pageEvent
               .getPage().getTemplate().getId());
   }

   public void observe(@Observes TemplateEvent templateEvent)
   {
      cacheService.cacheByTemplateId(templateEvent.getTemplate().getId());
   }
}
