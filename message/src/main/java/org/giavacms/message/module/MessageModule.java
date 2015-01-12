package org.giavacms.message.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MessageModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "message";
	}

	@Override
	public String getDescription() {
		return "Messaggi";
	}

	@Override
	public String getMenuFragment() {
		return "/private/message/message-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/message/message-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione messaggi");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("message", "gestione messaggi");
		return permissions;
	}
}
