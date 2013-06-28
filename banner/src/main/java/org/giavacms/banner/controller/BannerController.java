package org.giavacms.banner.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.banner.model.Banner;
import org.giavacms.banner.repository.BannerRepository;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;

@Named
@SessionScoped
public class BannerController extends AbstractLazyController<Banner> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/banner/view.xhtml";
	@ListPage
	public static String LIST = "/private/banner/list.xhtml";
	@EditPage
	public static String EDIT = "/private/banner/edit.xhtml";

	public static String EDIT_IMAGE = "/private/banner/edit-image.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(BannerRepository.class)
	BannerRepository bannerRepository;

	@Override
	public void initController() {
	}

	@Override
	public String update() {
		saveImage();
		return super.update();
	}

	public String updateAndModifyImage() {
		update();
		setEditMode(true);
		setReadOnlyMode(false);
		return EDIT_IMAGE + super.REDIRECT_PARAM;
	}

	@Override
	public String save() {
		saveImage();
		return super.save();
	}

	public String saveAndModifyImage() {
		save();
		setEditMode(true);
		setReadOnlyMode(false);
		return EDIT_IMAGE + super.REDIRECT_PARAM;
	}

	@Override
	public String delete() {
		super.delete();
		return listPage();
	}

	public String deleteImg() {
		getElement().setImage(null);
		return listPage();
	}

	public String modImage() {
		// TODO Auto-generated method stub
		super.modElement();
		return EDIT_IMAGE + super.REDIRECT_PARAM;
	}

	public String modImageCurrent() {
		// TODO Auto-generated method stub
		super.modCurrent();
		return EDIT_IMAGE + super.REDIRECT_PARAM;
	}

	private void saveImage() {
		if (getElement().getNewImage().getUploadedData() != null
				&& getElement().getNewImage().getUploadedData().getContents() != null
				&& getElement().getNewImage().getUploadedData().getFileName() != null
				&& !getElement().getNewImage().getUploadedData().getFileName()
						.isEmpty()) {
			logger.info("carico nuova immagine: "
					+ getElement().getNewImage().getUploadedData()
							.getFileName());
			Image img = new Image();
			img.setData(getElement().getNewImage().getUploadedData()
					.getContents());
			img.setType(getElement().getNewImage().getUploadedData()
					.getContentType());
			String filename = ResourceUtils.createImage_("img", getElement()
					.getNewImage().getUploadedData().getFileName(),
					getElement().getNewImage().getUploadedData().getContents());
			img.setFilename(filename);
			getElement().setImage(img);
			getElement().setNewImage(null);
		} else {
			logger.info("non c'e' nuova immagine");
		}
	}

	@Override
	public Object getId(Banner t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

}
