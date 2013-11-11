package org.giavacms.exhibition.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
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

   public PublicationRequestController()
   {
      super();
   }

   @Override
   public List<Publication> loadPage(int startRow, int pageSize)
   {
      Search<Publication> r = new Search<Publication>(Publication.class);
      r.getObj().setTitle(search);
      r.getObj().setAuthor(author);
      r.getObj().getExhibition().setId(exhibition);
      return publicationRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Publication> r = new Search<Publication>(Publication.class);
      r.getObj().setTitle(search);
      r.getObj().setAuthor(author);
      r.getObj().getExhibition().setId(exhibition);
      return publicationRepository.getListSize(r);
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
