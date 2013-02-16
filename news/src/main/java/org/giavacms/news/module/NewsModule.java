package org.giavacms.news.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;


@ApplicationScoped
public class NewsModule implements ModuleProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "news";
	}

	@Override
	public String getDescription() {
		return "News con possibilita' di caricare documenti e immagini";
	}

	@Override
	public String getMenuFragment() {
		return "/private/richnews/richnews-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/richnews/richnews-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione news");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("richnews", "gestione news");
		return permissions;
	}
}
