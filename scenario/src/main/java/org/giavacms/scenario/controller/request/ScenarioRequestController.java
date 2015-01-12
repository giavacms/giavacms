package org.giavacms.scenario.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.scenario.model.Scenario;
import org.giavacms.scenario.repository.ScenarioRepository;

@Named
@RequestScoped
public class ScenarioRequestController extends
         AbstractRequestController<Scenario> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String ID_PARAM = "id";
   public static final String CURRENT_PAGE_PARAM = "start";

   @Inject
   @HttpParam("scenario")
   String scenario;
   @Inject
   @HttpParam(ID_PARAM)
   String id;

   @Inject
   @HttpParam(CURRENT_PAGE_PARAM)
   String start;

   private List<Scenario> randomList = null;

   @Inject
   @OwnRepository(ScenarioRepository.class)
   ScenarioRepository scenarioRepository;

   @Override
   protected void initSearch() {
      getSearch().getObj().setTitle(scenario);
      super.initSearch();
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   @Override
   protected String getIdParam()
   {
      return ID_PARAM;
   }

   protected List<Scenario> loadRandomList(int pageSize)
   {
      if (randomList == null)
      {
         randomList = scenarioRepository.loadRandomList(pageSize);
      }

      return randomList;
   }
}
