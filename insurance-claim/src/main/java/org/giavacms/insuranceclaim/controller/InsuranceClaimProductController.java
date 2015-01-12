package org.giavacms.insuranceclaim.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ImageUtils;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.insuranceclaim.model.InsuranceClaimConfiguration;
import org.giavacms.insuranceclaim.model.InsuranceClaimProduct;
import org.giavacms.insuranceclaim.repository.InsuranceClaimConfigurationRepository;
import org.giavacms.insuranceclaim.repository.InsuranceClaimProductRepository;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class InsuranceClaimProductController extends
		AbstractLazyController<InsuranceClaimProduct> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/insuranceclaim/view.xhtml";
	@ListPage
	public static String LIST = "/private/insuranceclaim/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/insuranceclaim/edit.xhtml";

	public static String EDIT_DOCS = "/private/insuranceclaim/edit-documents.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(InsuranceClaimProductRepository.class)
	InsuranceClaimProductRepository insuranceClaimProductRepository;

	@Inject
	InsuranceClaimConfigurationRepository insuranceClaimConfigurationRepository;

	// --------------------------------------------------------

	public String modDocumentsCurrent() {
		// TODO Auto-generated method stub
		super.modCurrent();
		return EDIT_DOCS + REDIRECT_PARAM;
	}

	public String modDocuments() {
		super.modElement();
		return EDIT_DOCS + REDIRECT_PARAM;
	}

	public void handleUpload(FileUploadEvent event) {
		logger.info("Uploaded: " + event.getFile().getFileName() + " - "
				+ event.getFile().getContentType() + "- "
				+ event.getFile().getSize());
		String type = ResourceUtils.getType(event.getFile().getFileName());
		if (ResourceType.IMAGE.name().equals(type)) {
			handleImgUpload(event);
		} else {
			handleFileUpload(event);
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		Document doc = new Document();
		doc.setUploadedData(event.getFile());
		doc.setData(event.getFile().getContents());
		doc.setType(event.getFile().getContentType());
		String filename = ResourceUtils.createFile_("docs", event.getFile()
				.getFileName(), event.getFile().getContents());
		doc.setFilename(filename);
		getElement().getDocuments().add(doc);
	}

	public void handleImgUpload(FileUploadEvent event) {
		try {
			String type = event
					.getFile()
					.getFileName()
					.substring(
							event.getFile().getFileName().lastIndexOf(".") + 1);
			InsuranceClaimConfiguration insuranceClamsConfiguration = insuranceClaimConfigurationRepository
					.load();
			byte[] imgRes;
			if (insuranceClamsConfiguration.isResize()) {
				imgRes = ImageUtils
						.resizeImage(event.getFile().getContents(),
								insuranceClamsConfiguration
										.getMaxWidthOrHeight(), type);
			} else {
				imgRes = event.getFile().getContents();
			}
			Image img = new Image();
			img.setUploadedData(event.getFile());
			img.setData(imgRes);
			img.setType(event.getFile().getContentType());
			String filename = ResourceUtils.createImage_("img", event.getFile()
					.getFileName(), imgRes);
			img.setFilename(filename);
			getElement().getImages().add(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeDocument(Long id) {
		if (id != null && getElement() != null
				&& getElement().getDocuments() != null
				&& getElement().getDocuments().size() > 0) {
			List<Document> docsNew = new ArrayList<Document>();
			for (Document doc : getElement().getDocuments()) {
				if (doc.getId() != null && !doc.getId().equals(id)) {
					docsNew.add(doc);
				}
			}
			getElement().setDocuments(docsNew);
			insuranceClaimProductRepository.update(getElement());
		} else
			logger.info("removeImage: non posso rimuovere id:" + id);
	}

	public void removeImage(Long id) {
		if (id != null && getElement() != null
				&& getElement().getImages() != null
				&& getElement().getImages().size() > 0) {
			List<Image> imagesNew = new ArrayList<Image>();
			for (Image img : getElement().getImages()) {
				if (img.getId() != null && !img.getId().equals(id)) {
					imagesNew.add(img);
				}
			}
			getElement().setImages(imagesNew);
			insuranceClaimProductRepository.update(getElement());
		} else
			logger.info("removeImage: non posso rimuovere id:" + id);
	}

	// --------------------------------------------------------

	@Override
	public String save() {
		super.save();
		return super.viewPage();
	}

	public String saveAndmodDocuments() {
		save();
		setEditMode(true);
		setReadOnlyMode(false);
		return EDIT_DOCS + REDIRECT_PARAM;
	}

	@Override
	public String delete() {
		return super.delete();
	}

	@Override
	public String update() {
		super.update();
		return super.viewPage();
	}

	public String updateAndmodDocuments() {
		update();
		setEditMode(true);
		setReadOnlyMode(false);
		return EDIT_DOCS + REDIRECT_PARAM;
	}

}
