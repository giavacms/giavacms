package org.giavacms.news.producer;

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

import org.giavacms.news.model.type.NewsType;
import org.giavacms.news.repository.NewsTypeRepository;


@Named
@SessionScoped
public class NewsProducer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Class, SelectItem[]> items = null;

	@Inject
	NewsTypeRepository newsTypeRepository;

	@PostConstruct
	public void reset() {
		items = new HashMap<Class, SelectItem[]>();
	}

	@Named
	@Produces
	public SelectItem[] getRichNewsTypeItems() {
		if (items.get(NewsType.class) == null) {
			List<SelectItem> si = new ArrayList<SelectItem>();
			for (NewsType c : newsTypeRepository.getAllList()) {
				si.add(new SelectItem(c.getId(), c.getName()));
			}
			items.put(NewsType.class, si.toArray(new SelectItem[] {}));
		}
		return items.get(NewsType.class);
	}
}
