package org.giavacms.richcontent.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ExtensionProvider;
import org.giavacms.common.module.ModuleProvider;
import org.giavacms.richcontent.model.RichContent;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RichContentModule implements ModuleProvider, ExtensionProvider {

	Logger logger = Logger.getLogger(getClass());
	Properties permissions = null;

	@Override
	public String getName() {
		return "content";
	}

	@Override
	public String getDescription() {
		return "Contenuti con possibilita' di caricare documenti e immagini";
	}

	@Override
	public String getMenuFragment() {
		return "/private/richcontent/richcontent-menu.xhtml";
	}

	@Override
	public String getPanelFragment() {
		return "/private/richcontent/richcontent-panel.xhtml";
	}

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public List<String> getAllowableOperations() {
		List<String> list = new ArrayList<String>();
		list.add("gestione contenuti");
		return list;
	}

	@Override
	public Map<String, String> getPermissions() {
		Map<String, String> permissions = new HashMap<String, String>();
		permissions.put("richcontent", "gestione contenuti");
		return permissions;
	}

	@Override
	public List<String> getExtensions() {
		return Arrays.asList(RichContent.EXTENSION);
	}
}
