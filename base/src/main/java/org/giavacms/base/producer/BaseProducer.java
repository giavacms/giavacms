/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.Template;
import org.giavacms.base.repository.LanguageRepository;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateRepository;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.common.util.JSFUtils;
import org.jboss.logging.Logger;

@SessionScoped
@Named
@SuppressWarnings("rawtypes")
public class BaseProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	private PageRepository pageRepository;

	@Inject
	TemplateRepository templateRepository;
	@Inject
	LanguageRepository languageRepository;

	private Map<Class, SelectItem[]> items = null;

	private SelectItem[] fileTypeItems = new SelectItem[] {};
	private SelectItem[] tipiOperazioniLogItems = new SelectItem[] {};
	private SelectItem[] staticoDinamicoItems = new SelectItem[] {};

	public BaseProducer() {
	}

	@Produces
	@Named
	public SelectItem[] getTemplateItems() {
		if (items.get(Template.class) == null
				|| items.get(Template.class).length == 0) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "template..."));
			for (Template t : templateRepository.getList(new Search<Template>(
					Template.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getName()));
			}
			items.put(Template.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Template.class);
	}

	@Produces
	@Named
	public SelectItem[] getTemplateStaticiItems() {
		Search<Template> ricerca = new Search<Template>(Template.class);
		ricerca.getObj().setSearchStatico(true);
		return JSFUtils.setupItems(ricerca, templateRepository, "id", "name",
				"nessun template per pagine statiche disponibile",
				"seleziona template per pagine statiche...");
	}

	@Produces
	@Named
	public SelectItem[] getTemplateDinamiciItems() {
		Search<Template> ricerca = new Search<Template>(Template.class);
		ricerca.getObj().setSearchStatico(false);
		return JSFUtils.setupItems(ricerca, templateRepository, "id", "name",
				"nessun template per pagine dinamiche disponibile",
				"seleziona template per pagine dinamiche...");
	}

   public SelectItem[] getBasePageTemplateItems(String extensionType)
   {
      List<SelectItem> valori = new ArrayList<SelectItem>();
      valori.add(new SelectItem(null, "pagina base..."));
      List<Page> lista = pageRepository.getExtensions(extensionType);
      if (lista != null && lista.size() > 0)
      {
         for (Page p : lista)
         {
            valori.add(new SelectItem(p.getTemplateId(), p.getTitle()));
         }
      }
      return valori.toArray(new SelectItem[] {});
   }

	public SelectItem[] getBasePageItems(String extensionType) {
		List<SelectItem> valori = new ArrayList<SelectItem>();
		valori.add(new SelectItem(null, "pagina base..."));
		List<Page> lista = pageRepository.getExtensions(extensionType);
		if (lista != null && lista.size() > 0) {
			for (Page p : lista) {
				valori.add(new SelectItem(p.getId(), p.getTitle()));
			}
		}
		return valori.toArray(new SelectItem[] {});
	}

	public String getBasePageTitle(Long templateImplId) {
		try {
			return pageRepository.getBasePageTitleByTemplateImplId(
					templateImplId).toString();
		} catch (Exception e) {
			return "n.d.";
		}
	}

	public String getBasePageTitle(String pageId) {
		try {
			return pageRepository.find(pageId).getTitle();
		} catch (Exception e) {
			return "n.d.";
		}
	}

	@Produces
	@Named
	public SelectItem[] getStaticoDinamicoItems() {
		if (staticoDinamicoItems == null || staticoDinamicoItems.length == 0) {
			staticoDinamicoItems = new SelectItem[3];
			staticoDinamicoItems[0] = new SelectItem(null, "contenuto...");
			staticoDinamicoItems[1] = new SelectItem(true, "statico");
			staticoDinamicoItems[2] = new SelectItem(false, "dinamico");
		}
		return staticoDinamicoItems;
	}

	// NEW
	// DELETE
	// MODIFY
	// LOGIN
	// LOGOUT
	@Produces
	@Named
	public SelectItem[] getTipiOperazioniLogItems() {
		if (tipiOperazioniLogItems == null
				|| tipiOperazioniLogItems.length == 0) {
			tipiOperazioniLogItems = new SelectItem[6];
			tipiOperazioniLogItems[0] = new SelectItem(null, "operazione");
			tipiOperazioniLogItems[1] = new SelectItem("NEW", "NEW");
			tipiOperazioniLogItems[2] = new SelectItem("DELETE", "DELETE");
			tipiOperazioniLogItems[3] = new SelectItem("MODIFY", "MODIFY");
			tipiOperazioniLogItems[4] = new SelectItem("LOGIN", "LOGIN");
			tipiOperazioniLogItems[5] = new SelectItem("LOGOUT", "LOGOUT");
		}
		return tipiOperazioniLogItems;
	}

	@Produces
	@Named
	public SelectItem[] getFileTypeItems() {
		if (fileTypeItems == null || fileTypeItems.length == 0) {
			fileTypeItems = new SelectItem[6];
			fileTypeItems[0] = new SelectItem(0, "css");
			fileTypeItems[1] = new SelectItem(1, "img");
			fileTypeItems[2] = new SelectItem(2, "flash");
			fileTypeItems[3] = new SelectItem(3, "javascript");
			fileTypeItems[4] = new SelectItem(4, "docs");
			fileTypeItems[5] = new SelectItem(5, "font");
		}
		return fileTypeItems;
	}

	@Produces
	@Named
	public SelectItem[] getResourceItems() {
		return new SelectItem[] { new SelectItem("img", "immagini"),
				new SelectItem("js", "funzioni javascript"),
				new SelectItem("css", "fogli di stile"),
				new SelectItem("swf", "contenuti flash"),
				new SelectItem("docs", "documenti"),
				new SelectItem("font", "font") };
	}

	@Produces
	@Named
	public SelectItem[] getPageItems() {
		Search<Page> ricerca = new Search<Page>(Page.class);
		return checkItems(ricerca, pageRepository, "id", "title",
				"nessuna pagina disponibile", "seleziona pagina...");
	}

	public void setPageItems(SelectItem[] pageItems) {
		this.items.put(Page.class, pageItems);
	}

	@Produces
	@Named
	public SelectItem[] getSiNoItems() {
		List<SelectItem> valori = new ArrayList<SelectItem>();
		valori.add(new SelectItem(true, "SI"));
		valori.add(new SelectItem(false, "NO"));
		return valori.toArray(new SelectItem[] {});
	}

	@Produces
	@Named
	public SelectItem[] getSiNoNullItems() {
		List<SelectItem> valori = new ArrayList<SelectItem>();
		valori.add(new SelectItem(null, "..."));
		valori.add(new SelectItem(true, "SI"));
		valori.add(new SelectItem(false, "NO"));
		return valori.toArray(new SelectItem[] {});
	}

	private SelectItem[] checkItems(Search ricerca, AbstractRepository ejb,
			String idField, String valueField, String emptyMessage,
			String labelMessage) {
		Class clazz = ricerca.getObj().getClass();
		if (items.get(clazz) == null || items.get(clazz).length == 0) {
			items.put(clazz, JSFUtils.setupItems(ricerca, ejb, idField,
					valueField, emptyMessage, labelMessage));
		}
		return items.get(clazz);
	}

	// ==============================================================================

	@PostConstruct
	public void reset() {
		items = new HashMap<Class, SelectItem[]>();
	}

	public void resetItemsForClass(Class clazz) {
		if (items.containsKey(clazz)) {
			items.remove(clazz);
		}
	}

}
