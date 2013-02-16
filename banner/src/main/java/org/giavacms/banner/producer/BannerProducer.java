package org.giavacms.banner.producer;

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

import org.giavacms.banner.model.BannerTypology;
import org.giavacms.banner.repository.BannerTypologyRepository;
import org.giavacms.common.model.Search;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class BannerProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	private BannerTypologyRepository bannerTypologyRepository;

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	public BannerProducer() {
		// TODO Auto-generated constructor stub
	}

	@Produces
	@Named
	public SelectItem[] getBannerTypologyItems() {
		if (items.get(BannerTypology.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona tipologia..."));
			for (BannerTypology t : bannerTypologyRepository.getList(
					new Search<BannerTypology>(BannerTypology.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getName() + " - "
						+ t.getName()));
			}
			items.put(BannerTypology.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(BannerTypology.class);
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
