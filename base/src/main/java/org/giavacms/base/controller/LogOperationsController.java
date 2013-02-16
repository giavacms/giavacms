/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.OperazioniLog;
import org.giavacms.base.repository.LogOperationsRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;


@Named
@SessionScoped
public class LogOperationsController extends
         AbstractLazyController<OperazioniLog>
{

   // --------------------------------------------------------
   private static final long serialVersionUID = 1L;

   @BackPage
   public static final String BACK = "/private/administration.xhtml";

   @ViewPage
   public static final String VIEW = "/private/operations/view.xhtml";

   @ListPage
   public static final String LIST = "/private/operations/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(LogOperationsRepository.class)
   LogOperationsRepository operazioniLogRepository;

   // --------------------------------------------------------

   public LogOperationsController()
   {
   }

   // --------------------------------------------------------

   public void save(String tipo, String username, String descrizione)
   {
      operazioniLogRepository.persist(new OperazioniLog(tipo, username,
               descrizione, new Date()));
      reset();
   }

}
