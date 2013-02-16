package org.giavacms.catalogue.producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.jboss.logging.Logger;


@SessionScoped
@Named
public class CatalogueProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	private CategoryRepository categoryRepository;

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	public CatalogueProducer() {
		// TODO Auto-generated constructor stub
	}

	@Produces
	@Named
	public SelectItem[] getCategoryItems() {
		if (items.get(Category.class) == null) {
			items.put(Category.class, JSFUtils.setupItems(new Search<Category>(
					Category.class), categoryRepository, "id", "name",
					"nessuna categoria", "seleziona categoria..."));
		}
		return items.get(Category.class);
	}

	public void resetItemsForClass(Class clazz) {
		if (items.containsKey(clazz)) {
			items.remove(clazz);
		}
	}

	// ==============================================================================

	@PostConstruct
	public void reset() {
		items = new HashMap<Class, SelectItem[]>();
	}

}
