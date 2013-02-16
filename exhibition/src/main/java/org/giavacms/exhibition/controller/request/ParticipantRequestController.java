package org.giavacms.exhibition.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.exhibition.model.Discipline;
import org.giavacms.exhibition.model.Exhibition;
import org.giavacms.exhibition.model.Participant;
import org.giavacms.exhibition.model.pojo.ParticipantExhibition;
import org.giavacms.exhibition.repository.ExhibitionRepository;
import org.giavacms.exhibition.repository.ParticipantRepository;

@Named
@RequestScoped
public class ParticipantRequestController extends
         AbstractRequestController<Participant> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String EXHIBITION = "exhibition";
   public static final String DISCIPLINE = "discipline";
   public static final String YEAR = "year";
   public static final String SUBJECT = "subject";
   public static final String SEARCH = "q";
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";
   public static final String[] PARAM_NAMES = new String[] { ID_PARAM,
            EXHIBITION, YEAR, SUBJECT, DISCIPLINE, SEARCH, ID_PARAM, CURRENT_PAGE_PARAM };

   private String filter;
   private Exhibition latestExhibition;

   @Inject
   @OwnRepository(ParticipantRepository.class)
   ParticipantRepository participantRepository;

   @Inject
   ExhibitionRepository exhibitionRepository;

   public ParticipantRequestController()
   {
      super();
   }

   @Override
   protected void init()
   {
      super.init();
   }

   @Override
   public List<Participant> loadPage(int startRow, int pageSize)
   {
      Search<Participant> r = new Search<Participant>(Participant.class);
      r.getObj().getDiscipline().setId(getParams().get(DISCIPLINE));
      if (getParams().get(EXHIBITION) != null && !getParams().get(EXHIBITION).isEmpty())
      {
         r.getObj().getExhibition().setId(getParams().get(EXHIBITION));
      }
      else
      {
         if (getLatestExhibition() != null)
            r.getObj().getExhibition().setId(getLatestExhibition().getId());
      }
      r.getObj().getSubject().setId(getParams().get(SUBJECT));
      r.getObj().getSubject().setSurname(getParams().get(SEARCH));
      if (getFilter() != null && !getFilter().isEmpty())
      {
         r.getObj().getSubject().setType(getFilter());
      }
      return participantRepository.getList(r, startRow, pageSize);
   }

   public List<ParticipantExhibition> getAllPartecipantsBySubject()
   {
      return participantRepository.getAllPartecipantsBySubject(getElement().getSubject().getId());
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Participant> r = new Search<Participant>(Participant.class);
      r.getObj().getDiscipline().setId(getParams().get(DISCIPLINE));
      if (getParams().get(EXHIBITION) != null && !getParams().get(EXHIBITION).isEmpty())
      {
         r.getObj().getExhibition().setId(getParams().get(EXHIBITION));
      }
      else
      {
         if (getLatestExhibition() != null)
            r.getObj().getExhibition().setId(getLatestExhibition().getId());
      }
      r.getObj().getSubject().setId(getParams().get(SUBJECT));
      r.getObj().getSubject().setSurname(getParams().get(SEARCH));
      if (getFilter() != null && !getFilter().isEmpty())
      {
         r.getObj().getSubject().setType(getFilter());
      }
      return participantRepository.getListSize(r);
   }

   public List<Participant> getPageOfSizeWithType(int size, String type)
   {
      setFilter(type);
      setPageSize(size);
      return getPage();
   }

   public String getFilter()
   {
      return filter;
   }

   public void setFilter(String filter)
   {
      this.filter = filter;
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

   public String viewElement(Long id)
   {
      setElement(participantRepository.fetch(id));
      return viewPage();
   }

   public List<Discipline> getDiscipline()
   {
      return participantRepository.getDistinctDiscipline(getParams().get(
               EXHIBITION));
   }

   @Override
   public Participant getElement()
   {
      // TODO Auto-generated method stub
      if (super.getElement() == null)
      {
         Participant element = participantRepository
                  .findLatestPartcipantBySubjectId(getParams().get(SUBJECT));
         setElement(element);
      }
      return element;
   }

   public Exhibition getLatestExhibition()
   {
      if (latestExhibition == null)
         this.latestExhibition = exhibitionRepository.getLatest();
      return latestExhibition;
   }

}
