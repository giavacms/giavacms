package org.giavacms.insuranceclaim.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.insuranceclaim.model.InsuranceClaimCategory;
import org.giavacms.insuranceclaim.producer.InsuranceClaimProducer;
import org.giavacms.insuranceclaim.repository.InsuranceClaimCategoryRepository;

@Named
@SessionScoped
public class InsuranceClaimCategoryController extends
		AbstractLazyController<InsuranceClaimCategory> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/insuranceclaim/category/view.xhtml";
	@ListPage
	public static String LIST = "/private/insuranceclaim/category/list.xhtml";
	@EditPage
	public static String EDIT = "/private/insuranceclaim/category/edit.xhtml";

	public static String EDIT_IMAGE = "/private/insuranceclaim/category/edit-image.xhtml";

	// ------------------------------------------------
	@Inject
	@OwnRepository(InsuranceClaimCategoryRepository.class)
	InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

	@Inject
	InsuranceClaimProducer insuranceClaimProducer;

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
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	@Override
	public String save() {
		saveImage();
		insuranceClaimProducer.reset();
		return super.save();
	}

	public String saveAndModifyImage() {
		saveImage();
		setEditMode(true);
		setReadOnlyMode(false);
		insuranceClaimProducer.reset();
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	@Override
	public String delete() {
		super.delete();
		insuranceClaimProducer.reset();
		return listPage();
	}

	public String deleteImg() {
		getElement().setImage(null);
		insuranceClaimCategoryRepository.update(getElement());
		return listPage();
	}

	public String modImage() {
		// TODO Auto-generated method stub
		super.modElement();
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	public String modImageCurrent() {
		// TODO Auto-generated method stub
		super.modCurrent();
		return EDIT_IMAGE + REDIRECT_PARAM;
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
	public Object getId(InsuranceClaimCategory t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public String reset() {
		insuranceClaimProducer.reset();
		return super.reset();
	}
}
