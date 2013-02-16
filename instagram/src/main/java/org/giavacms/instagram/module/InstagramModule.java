package org.giavacms.instagram.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;


@ApplicationScoped
public class InstagramModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "instagram";
	}

	@Override
	public String getDescription() {
		return "Instagram Collections";
	}

	@Override
	public String getMenuFragment() {
		return "/private/instagram/instagram-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/instagram/instagram-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione instagram collection");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("instagram", "gestione instagram collection");
		return permissions;
	}
}
