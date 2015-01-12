package org.giavacms.banner.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class BannerModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "banner";
	}

	@Override
	public String getDescription() {
		return "Banner";
	}

	@Override
	public String getMenuFragment() {
		return "/private/banner/banner-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/banner/banner-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione banner");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("banner", "gestione banner");
		return permissions;
	}
}
