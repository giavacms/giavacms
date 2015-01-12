package org.giavacms.instagram.producer;

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
import org.giavacms.instagram.model.InstagramCollection;
import org.giavacms.instagram.repository.InstagramCollectionRepository;
import org.jboss.logging.Logger;


@SessionScoped
@Named
public class InstagramCollectionProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	InstagramCollectionRepository instagramCollectionRepository;

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	public InstagramCollectionProducer() {
		// TODO Auto-generated constructor stub
	}

	@Produces
	@Named
	public SelectItem[] getInstagramCollectionItems() {
		if (items.get(InstagramCollection.class) == null) {
			items.put(InstagramCollection.class, JSFUtils.setupItems(
					new Search<InstagramCollection>(InstagramCollection.class),
					instagramCollectionRepository, "id", "name", "nessuna categoria",
					"seleziona collection..."));
		}
		return items.get(InstagramCollection.class);
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
