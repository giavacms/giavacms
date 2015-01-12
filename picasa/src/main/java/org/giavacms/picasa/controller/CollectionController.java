package org.giavacms.picasa.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.picasa.model.Album;
import org.giavacms.picasa.model.Collection;
import org.giavacms.picasa.model.Photo;
import org.giavacms.picasa.producer.CollectionProducer;
import org.giavacms.picasa.repository.CollectionRepository;
import org.giavacms.picasa.service.PicasaAtomService;


@Named
@SessionScoped
public class CollectionController extends AbstractLazyController<Collection> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/picasa/view.xhtml";
	@EditPage
	public static String EDIT = "/private/picasa/edit.xhtml";
	@ListPage
	public static String LIST = "/private/picasa/list.xhtml";

	// ------------------------------------------------

	private Album album;
	private List<Photo> albumPhotos;
	private List<Photo> photos;

	@Inject
	@OwnRepository(CollectionRepository.class)
	CollectionRepository collectionRepository;

	@Inject
	CollectionProducer collectionProducer;

	@Inject
	PicasaAtomService picasaAtomService;

	@Override
	public void initController() {
		if (getElement() == null) {
			setElement(new Collection());
		}
	}

	@Override
	public Object getId(Collection t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public String reset() {
		collectionProducer.reset();
		return super.reset();
	}

	@Override
	public String addElement() {
		this.photos = null;
		return super.addElement();
	}

	@Override
	public String modElement() {
		// TODO Auto-generated method stub
		super.modElement();
		setPhotos(getElement().getPhotos());
		return editPage();
	}

	@Override
	public String update() {
		getElement().setPhotos(null);
		collectionRepository.update(getElement());
		for (Photo photo : getPhotos()) {
			if (photo.isCover())
				getElement().setCoverUrl(photo.getImageUrl());
		}
		getElement().setPhotos(getPhotos());
		collectionProducer.reset();
		return super.update();
	}

	@Override
	public String save() {
		for (Photo photo : getPhotos()) {
			if (photo.isCover())
				getElement().setCoverUrl(photo.getImageUrl());
		}
		getElement().setDate(new Date());
		getElement().setPhotos(getPhotos());
		getElement().setActive(true);
		collectionProducer.reset();
		return super.save();
	}

	// ------------------------------------------------

	public Album getAlbum() {
		if (album == null)
			this.album = new Album();
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public List<Photo> getPhotos() {
		if (photos == null)
			this.photos = new ArrayList<Photo>();
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public void addPhoto(Photo photo) {
		getPhotos().add(photo);
	}

	public void useLikeCover(long index) {
		for (Photo photo : getPhotos()) {
			if (photo.getIndex() != index) {
				photo.setCover(false);
			} else {
				photo.setCover(true);
			}
		}
	}

	public void removeFromPhotos(long index) {
		int i = 0;
		for (Photo photo : getPhotos()) {
			if (photo.getIndex() == index) {
				break;
			}
			i++;
		}
		getPhotos().remove(i);
	}

	public List<Photo> getAlbumPhotos() {
		return albumPhotos;
	}

	public void setAlbumPhotos(List<Photo> albumPhotos) {
		this.albumPhotos = albumPhotos;
	}

	public void loadAlbumPhotos() {
		List<Photo> fromAlbum;
		try {
			fromAlbum = picasaAtomService.getPhotoByAlbum(getAlbum()
					.getFeedLink());
			setAlbumPhotos(fromAlbum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPhotoToList(long index) {
		for (Photo photo : getAlbumPhotos()) {
			if (photo.getIndex() == index) {
				photo.setCollection(getElement());
				getPhotos().add(photo);
				return;
			}
		}
	}

}
