package org.giavacms.twizz.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.twizz.model.Partecipation;
import org.giavacms.twizz.repository.PartecipationRepository;

@RequestScoped
@Named
public class HitParadeController extends AbstractRequestController<Partecipation>
         implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String SEARCH = "q";
   public static final String[] PARAM_NAMES = new String[] { SEARCH };
   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @OwnRepository(PartecipationRepository.class)
   PartecipationRepository partecipationRepository;

   public HitParadeController()
   {
      super();
   }

   @Override
   public List<Partecipation> loadPage(int startRow, int pageSize)
   {
      Search<Partecipation> r = new Search<Partecipation>(Partecipation.class);
      r.getObj().getQuizCompetitor().setFullName(getParams().get(SEARCH));
      return partecipationRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Partecipation> r = new Search<Partecipation>(Partecipation.class);
      r.getObj().getQuizCompetitor().setFullName(getParams().get(SEARCH));
      return partecipationRepository.getListSize(r);
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

}
