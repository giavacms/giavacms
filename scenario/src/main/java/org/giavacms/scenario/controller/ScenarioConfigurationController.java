package org.giavacms.scenario.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.scenario.model.ScenarioConfiguration;
import org.giavacms.scenario.repository.ScenarioConfigurationRepository;


@Named
@SessionScoped
public class ScenarioConfigurationController extends
		AbstractLazyController<ScenarioConfiguration> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/scenario/configuration.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(ScenarioConfigurationRepository.class)
	ScenarioConfigurationRepository scenarioConfigurationRepository;

	@Override
	public ScenarioConfiguration getElement() {
		if (super.getElement() == null)
			setElement(scenarioConfigurationRepository.load());
		return super.getElement();
	}
}
