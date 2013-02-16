package org.giavacms.instagram.controller;

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
import org.giavacms.instagram.model.InstagramCollection;
import org.giavacms.instagram.model.InstagramPhoto;
import org.giavacms.instagram.producer.InstagramCollectionProducer;
import org.giavacms.instagram.repository.InstagramCollectionRepository;
import org.giavacms.instagram.service.InstagramService;


@Named
@SessionScoped
public class InstagramCollectionController extends
		AbstractLazyController<InstagramCollection> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/instagram/view.xhtml";
	@EditPage
	public static String EDIT = "/private/instagram/edit.xhtml";
	@ListPage
	public static String LIST = "/private/instagram/list.xhtml";

	// ------------------------------------------------

	private List<InstagramPhoto> albumPhotos;
	private List<InstagramPhoto> photos;

	@Inject
	@OwnRepository(InstagramCollectionRepository.class)
	InstagramCollectionRepository instagramCollectionRepository;

	@Inject
	InstagramCollectionProducer instagramCollectionProducer;

	@Inject
	InstagramService instagramService;

	@Override
	public void initController() {
		if (getElement() == null) {
			setElement(new InstagramCollection());
		}
	}

	@Override
	public Object getId(InstagramCollection t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public String reset() {
		instagramCollectionProducer.reset();
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
		instagramCollectionRepository.update(getElement());
		for (InstagramPhoto photo : getPhotos()) {
			if (photo.isCover())
				getElement().setCoverUrl(photo.getImageUrl());
		}
		getElement().setPhotos(getPhotos());
		instagramCollectionProducer.reset();
		return super.update();
	}

	@Override
	public String save() {
		for (InstagramPhoto photo : getPhotos()) {
			if (photo.isCover())
				getElement().setCoverUrl(photo.getImageUrl());
		}
		getElement().setDate(new Date());
		getElement().setPhotos(getPhotos());
		getElement().setActive(true);
		instagramCollectionProducer.reset();
		return super.save();
	}

	// ------------------------------------------------

	public List<InstagramPhoto> getPhotos() {
		if (photos == null)
			this.photos = new ArrayList<InstagramPhoto>();
		return photos;
	}

	public void setPhotos(List<InstagramPhoto> photos) {
		this.photos = photos;
	}

	public void addPhoto(InstagramPhoto photo) {
		getPhotos().add(photo);
	}

	public void useLikeCover(long index) {
		for (InstagramPhoto photo : getPhotos()) {
			if (photo.getIndex() != index) {
				photo.setCover(false);
			} else {
				photo.setCover(true);
			}
		}
	}

	public void removeFromPhotos(long index) {
		int i = 0;
		for (InstagramPhoto photo : getPhotos()) {
			if (photo.getIndex() == index) {
				break;
			}
			i++;
		}
		getPhotos().remove(i);
	}

	public List<InstagramPhoto> getAlbumPhotos() {
		return albumPhotos;
	}

	public void setAlbumPhotos(List<InstagramPhoto> albumPhotos) {
		this.albumPhotos = albumPhotos;
	}

	public void loadAlbumPhotos() {
		List<InstagramPhoto> fromAlbum;
		try {
			fromAlbum = null;
			setAlbumPhotos(fromAlbum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPhotoToList(long index) {
		for (InstagramPhoto photo : getAlbumPhotos()) {
			if (photo.getIndex() == index) {
				photo.setCollection(getElement());
				getPhotos().add(photo);
				return;
			}
		}
	}

}
