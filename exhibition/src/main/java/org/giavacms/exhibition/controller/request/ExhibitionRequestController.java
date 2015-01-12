/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.exhibition.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.exhibition.model.Exhibition;
import org.giavacms.exhibition.repository.ExhibitionRepository;

@Named
@RequestScoped
public class ExhibitionRequestController extends
         AbstractRequestController<Exhibition> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String EXHIBITION = "exhibition";
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { EXHIBITION,
            SEARCH };
   public static final String ID_PARAM = "exhibition";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @HttpParam(EXHIBITION)
   String exhibition;

   @Inject
   @HttpParam(SEARCH)
   String search;

   @Inject
   @OwnRepository(ExhibitionRepository.class)
   ExhibitionRepository exhibitionRepository;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setName(search);
      super.initSearch();
   }

   public List<Exhibition> getAll()
   {
      return exhibitionRepository.getAll();
   }

   @Override
   public String getIdParam()
   {
      return ID_PARAM;
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   public boolean isScheda()
   {
      return getElement() != null && getElement().getId() != null;
   }

}
