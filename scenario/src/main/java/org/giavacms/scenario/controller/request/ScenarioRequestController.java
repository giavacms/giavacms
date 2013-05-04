package org.giavacms.scenario.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.scenario.model.Scenario;
import org.giavacms.scenario.repository.ScenarioRepository;


@Named
@RequestScoped
public class ScenarioRequestController extends
		AbstractRequestController<Scenario> implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String ID_PARAM = "id";
	public static final String SCENARIO = "scenario";
	public static final String CURRENT_PAGE_PARAM = "start";
	public static final String[] PARAM_NAMES = new String[] { SCENARIO };
	private List<Scenario> randomList = null;
	@Inject
	@OwnRepository(ScenarioRepository.class)
	ScenarioRepository scenarioRepository;

	public ScenarioRequestController() {

	}

	@Override
	public int totalSize() {
		// siamo all'interno della stessa richiesta per servire la quale è
		// avvenuta la postconstruct
		Search<Scenario> r = new Search<Scenario>(Scenario.class);
		r.getObj().setTitle(getParams().get(SCENARIO));
		return scenarioRepository.getListSize(r);
	}

	@Override
	public String getCurrentPageParam() {
		return CURRENT_PAGE_PARAM;
	}

	@Override
	protected String[] getParamNames() {
		return PARAM_NAMES;
	}

	@Override
	protected String getIdParam() {
		return ID_PARAM;
	}

	@Override
	protected List<Scenario> loadPage(int startRow, int pageSize) {
		Search<Scenario> r = new Search<Scenario>(Scenario.class);
		r.getObj().setTitle(getParams().get(SCENARIO));
		return scenarioRepository.getList(r, startRow, pageSize);
	}

	protected List<Scenario> loadRandomList(int pageSize) {
		if (randomList == null) {
			Search<Scenario> r = new Search<Scenario>(Scenario.class);
			r.getObj().setTitle(getParams().get(SCENARIO));
			randomList = scenarioRepository.loadRandomList(pageSize);
		}

		return randomList;
	}
}
