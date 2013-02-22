package org.giavacms.errors.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ErrorPagesModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "errorpages";
	}

	@Override
	public String getDescription() {
		return "Pagine di errore";
	}

	@Override
	public String getMenuFragment() {
		return "/private/errorpages/errorpages-menu.xhtml";
//		return "/private/errors/errors-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/errorpages/errorpages-panel.xhtml";
//		return "/private/errors/errors-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione pagine di errore");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("errors", "gestione pagine di errore");
		return permissions;
	}
}
