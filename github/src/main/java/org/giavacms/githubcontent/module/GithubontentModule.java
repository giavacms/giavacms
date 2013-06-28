package org.giavacms.githubcontent.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GithubontentModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "githubcontent";
	}

	@Override
	public String getDescription() {
		return "Contenuti da github";
	}

	@Override
	public String getMenuFragment() {
		return "/private/githubcontent/githubcontent-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/githubcontent/githubcontent-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione githubcontent");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("githubcontent", "gestione githubcontent");
		return permissions;
	}

}
