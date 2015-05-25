package org.giavacms.scenario.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.scenario.management.AppConstants;
import org.giavacms.scenario.model.Scenario;
import org.giavacms.scenario.repository.ScenarioRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.SCENARIO_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScenarioRepositoryRs extends RsRepositoryService<Scenario>
{

   private static final long serialVersionUID = 1L;

   @Inject
   public ScenarioRepositoryRs(ScenarioRepository scenarioRepository)
   {
      super(scenarioRepository);
   }

   public ScenarioRepositoryRs()
   {
   }

}
