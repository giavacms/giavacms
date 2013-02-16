package org.giavacms.contactus.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactUsModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "contactus";
	}

	@Override
	public String getDescription() {
		return "Contatti";
	}

	@Override
	public String getMenuFragment() {
		return "/private/contactus/contactus-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/contactus/contactus-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 30;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione contatti");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("contactus", "gestione contatti");
		return permissions;
	}
}
