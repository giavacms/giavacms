package org.giavacms.catalogue.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.common.util.ImageUtils;
import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateImplRepository;
import org.giavacms.catalogue.model.CatalogueConfiguration;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CatalogueConfigurationRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class ProductController extends AbstractPageController<Product> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/catalogue/view.xhtml";
	@ListPage
	public static String LIST = "/private/catalogue/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/catalogue/edit.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(ProductRepository.class)
	ProductRepository prodottiRepository;

	@Inject
	CatalogueConfigurationRepository catalogueConfigurationRepository;

	@Inject
	TemplateImplRepository templateImplRepository;

	@Inject
	PageRepository pageRepository;

	@Override
	public String getExtension()
	{
	   return Product.EXTENSION;
	}
	
	// --------------------------------------------------------

	public void handleFileUpload(FileUploadEvent event) {
		logger.info("Uploaded: " + event.getFile().getFileName() + " - "
				+ event.getFile().getContentType() + "- "
				+ event.getFile().getSize());
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
		logger.info("Uploaded: " + event.getFile().getFileName() + " - "
				+ event.getFile().getContentType() + "- "
				+ event.getFile().getSize());
		try {
			String type = event
					.getFile()
					.getFileName()
					.substring(
							event.getFile().getFileName().lastIndexOf(".") + 1);
			CatalogueConfiguration catalogueConfiguration = catalogueConfigurationRepository
					.load();
			byte[] imgRes;
			if (catalogueConfiguration.isResize()) {
				imgRes = ImageUtils.resizeImage(event.getFile().getContents(),
						catalogueConfiguration.getMaxWidthOrHeight(), type);
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
			prodottiRepository.update(getElement());
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
			prodottiRepository.update(getElement());
		} else
			logger.info("removeImage: non posso rimuovere id:" + id);
	}

	// --------------------------------------------------------

	@Override
	public String save() {
		if (super.save() == null) {
			return null;
		}
		setElement(getRepository().fetch(getElement().getId()));
		return super.viewPage();
	}

	@Override
	public String update() {
		if (super.update() == null) {
			return null;
		}
		setElement(getRepository().fetch(getElement().getId()));
		return super.viewPage();
	}

}
