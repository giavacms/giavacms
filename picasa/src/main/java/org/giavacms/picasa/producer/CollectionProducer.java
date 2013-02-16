package org.giavacms.picasa.producer;

import java.io.IOException;
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

import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.picasa.model.Album;
import org.giavacms.picasa.model.Collection;
import org.giavacms.picasa.repository.CollectionRepository;
import org.giavacms.picasa.service.PicasaAtomService;
import org.jboss.logging.Logger;


@SessionScoped
@Named
public class CollectionProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	CollectionRepository collectionRepository;

	@Inject
	PicasaAtomService picasaAtomService;

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	public CollectionProducer() {
		// TODO Auto-generated constructor stub
	}

	@Produces
	@Named
	public SelectItem[] getAlbumItems() {
		if (items.get(Album.class) == null) {
			List<Album> albums;
			try {
				albums = picasaAtomService.getAlbumList();
				List<SelectItem> albumItems = new ArrayList<SelectItem>();
				for (Album album : albums) {
					albumItems.add(new SelectItem(album.getFeedLink(), album
							.getTitle()));
				}
				items.put(Album.class, albumItems.toArray(new SelectItem[] {}));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return items.get(Album.class);
	}

	@Produces
	@Named
	public SelectItem[] getCollectionItems() {
		if (items.get(Collection.class) == null) {
			items.put(Collection.class, JSFUtils.setupItems(
					new Search<Collection>(Collection.class),
					collectionRepository, "id", "name", "nessuna categoria",
					"seleziona collection..."));
		}
		return items.get(Collection.class);
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
