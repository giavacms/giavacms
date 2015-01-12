/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.service;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.base.model.OperazioniLog;
import org.giavacms.base.repository.LogOperationsRepository;
import org.giavacms.common.interceptor.LogWriter;
import org.giavacms.common.util.JSFUtils;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class LogWriterService implements LogWriter
{

   @Inject
   LogOperationsRepository logOperationsRepository;

   Logger logger = Logger.getLogger(getClass());

   @Override
   public void write(String className, String method, String params,
            Object result)
   {
      if (className.contains("LogOperationsRepository"))
         return;
      String tipo = "";
      if (method.contains("persist"))
      {
         tipo = OperazioniLog.NEW;
      }
      else if (method.contains("update"))
      {
         tipo = OperazioniLog.MODIFY;
      }
      else if (method.contains("delete"))
      {
         tipo = OperazioniLog.DELETE;
      }
      else if (method.contains("logout"))
      {
         tipo = OperazioniLog.LOGOUT;
      }
      else if (method.contains("login"))
      {
         tipo = OperazioniLog.LOGIN;
      }
      String username = "anonymous";
      try
      {
         username = JSFUtils.getUserName();
      }
      catch (Throwable t)
      {
         logger.error(t.getClass().getCanonicalName() + " - " + t.getMessage());
      }
      String descrizione = "CLASSNAME: " + className + " - METHOD: " + method
               + "PARAMS: " + params + " - POST: "
               + (result != null ? result.toString() : "");
      Date data = new Date();
      OperazioniLog operazioniLog = new OperazioniLog(tipo, username,
               descrizione, data);
      logOperationsRepository.persist(operazioniLog);
   }
}
