package org.giavacms.exhibition.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.exhibition.model.Museum;
import org.giavacms.exhibition.model.Participant;
import org.giavacms.exhibition.repository.MuseumRepository;
import org.giavacms.exhibition.service.ParticipantService;

@Named
@RequestScoped
public class MuseumRequestController extends AbstractRequestController<Museum>
         implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { SEARCH };
   public static final String ID_PARAM = "museum";
   public static final String EXHIBITION = "exhibition";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(MuseumRepository.class)
   MuseumRepository museumRepository;

   @Inject
   ParticipantService participantService;

   public MuseumRequestController()
   {
      super();
   }

   @Override
   public List<Museum> loadPage(int startRow, int pageSize)
   {
      Search<Participant> r = new Search<Participant>(Participant.class);
      r.getObj().getSubject().setType(Museum.TYPE);
      r.getObj().getExhibition().setId(getParams().get(EXHIBITION));
      r.getObj().getSubject().setSurname(getParams().get(SEARCH));
      return (List<Museum>) participantService.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Participant> r = new Search<Participant>(Participant.class);
      r.getObj().getSubject().setType(Museum.TYPE);
      r.getObj().getExhibition().setId(getParams().get(EXHIBITION));
      r.getObj().getSubject().setSurname(getParams().get(SEARCH));
      return participantService.getListSize(r);
   }

   @Override
   public String[] getParamNames()
   {
      return PARAM_NAMES;
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
