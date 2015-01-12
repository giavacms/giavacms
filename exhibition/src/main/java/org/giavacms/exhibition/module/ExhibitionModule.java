package org.giavacms.exhibition.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExhibitionModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "exhibition";
	}

	@Override
	public String getDescription() {
		return "Exhibition";
	}

	@Override
	public String getMenuFragment() {
		return "/private/exhibition/exhibition-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/exhibition/exhibition-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione esposizioni");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("exhibition", "gestione esposizioni");
		return permissions;
	}
}
