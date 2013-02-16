package org.giavacms.richnews.producer;

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
import org.giavacms.richnews.model.type.RichNewsType;
import org.giavacms.richnews.repository.RichNewsTypeRepository;
import org.jboss.logging.Logger;


@Named
@SessionScoped
public class RichNewsProducer implements Serializable {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = Logger.getLogger(getClass()
			.getCanonicalName());

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	@Inject
	RichNewsTypeRepository richNewsTypeRepository;

	// ==============================================================================
	public RichNewsProducer() {

	}

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void reset() {
		logger.info("reset");
		items = new HashMap<Class, SelectItem[]>();
	}

	public void resetItemsForClass(Class clazz) {
		if (items.containsKey(clazz)) {
			items.remove(clazz);
		}
	}

	@Produces
	@Named
	public SelectItem[] getRichNewsTypeItems() {
		if (items.get(RichNewsType.class) == null) {
			items.put(RichNewsType.class, JSFUtils.setupItems(
					new Search<RichNewsType>(RichNewsType.class),
					richNewsTypeRepository, "id", "name", "nessuna categoria",
					"seleziona tipo news..."));
		}
		return items.get(RichNewsType.class);
	}
}
