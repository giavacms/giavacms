package org.giavacms.exhibition.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.exhibition.model.Publication;
import org.giavacms.exhibition.repository.PublicationRepository;

@Named
@RequestScoped
public class PublicationRequestController extends
         AbstractRequestController<Publication> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String EXHIBITION = "exhibition";
   public static final String AUTHOR = "autor";
   public static final String SEARCH = "q";
   public static final String CURRENT_PAGE_PARAM = "start";
   public static final String ID_PARAM = "id";

   @Inject
   @OwnRepository(PublicationRepository.class)
   PublicationRepository publicationRepository;

   @Inject
   @HttpParam(EXHIBITION)
   String exhibition;

   @Inject
   @HttpParam(AUTHOR)
   String author;

   @Inject
   @HttpParam(SEARCH)
   String search;

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setTitle(search);
      getSearch().getObj().setAuthor(author);
      getSearch().getObj().getExhibition().setId(exhibition);
      super.initSearch();
   }

   @Override
   protected String getIdParam()
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
