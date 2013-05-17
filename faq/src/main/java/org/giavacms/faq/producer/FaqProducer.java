package org.giavacms.faq.producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.faq.model.FaqCategory;
import org.giavacms.faq.repository.FaqCategoryRepository;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class FaqProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	private FaqCategoryRepository faqCategoryRepository;

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	public FaqProducer() {
		// TODO Auto-generated constructor stub
	}

	@Produces
	@Named
	public SelectItem[] getFaqCategoryItems() {
		if (items.get(FaqCategory.class) == null) {
			Search<FaqCategory> search = new Search<FaqCategory>(
					FaqCategory.class);
			items.put(FaqCategory.class, JSFUtils.setupItems(
					new Search<FaqCategory>(FaqCategory.class),
					faqCategoryRepository, "id", "title", "nessuna categoria",
					"seleziona categoria..."));
		}
		return items.get(FaqCategory.class);
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
