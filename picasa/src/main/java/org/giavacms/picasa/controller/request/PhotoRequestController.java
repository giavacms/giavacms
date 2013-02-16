package org.giavacms.picasa.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.picasa.model.Collection;
import org.giavacms.picasa.model.Photo;
import org.giavacms.picasa.repository.CollectionRepository;
import org.giavacms.picasa.repository.PhotoRepository;


@Named
@RequestScoped
public class PhotoRequestController extends AbstractRequestController<Photo>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String COLLECTION = "collection";
	public static final String TAG = "tag";
	public static final String[] PARAM_NAMES = new String[] { COLLECTION, TAG };
	public static final String ID_PARAM = "id";

	public static final String CURRENT_PAGE_PARAM = "start";
	private Collection collection;

	@Inject
	CollectionRepository collectionRepository;

	@Inject
	@OwnRepository(PhotoRepository.class)
	PhotoRepository photoRepository;

	public PhotoRequestController() {
		super();
	}

	@Override
	protected void init() {
		super.init();
		setPageSize(20);
	}

	@Override
	public List<Photo> loadPage(int startRow, int pageSize) {
		Search<Photo> r = new Search<Photo>(Photo.class);
		r.getObj().getCollection().setId(getParams().get(COLLECTION));
		r.getObj().getCollection().setTags(getParams().get(TAG));
		return photoRepository.getList(r, startRow, pageSize);
	}

	@Override
	public int totalSize() {
		// siamo all'interno della stessa richiesta per servire la quale è
		// avvenuta la postconstruct
		Search<Photo> r = new Search<Photo>(Photo.class);
		r.getObj().getCollection().setId(getParams().get(COLLECTION));
		r.getObj().getCollection().setTags(getParams().get(TAG));
		return photoRepository.getListSize(r);
	}

	@Override
	public String[] getParamNames() {
		return PARAM_NAMES;
	}

	@Override
	public String getIdParam() {
		return ID_PARAM;
	}

	@Override
	public String getCurrentPageParam() {
		return CURRENT_PAGE_PARAM;
	}

	public Collection getCollection() {
		if (getParams().get(COLLECTION) != null) {
			this.collection = collectionRepository.fetch(getParams().get(
					COLLECTION));
		}
		return collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

}
