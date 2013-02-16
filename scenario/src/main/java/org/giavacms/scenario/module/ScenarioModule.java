package org.giavacms.scenario.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;


@ApplicationScoped
public class ScenarioModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "scenario";
	}

	@Override
	public String getDescription() {
		return "Realizzazioni con prodotti collegati";
	}

	@Override
	public String getMenuFragment() {
		return "/private/scenario/scenario-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/scenario/scenario-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 15;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione scenario");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("scenario", "gestione scenario");
		return permissions;
	}
}
