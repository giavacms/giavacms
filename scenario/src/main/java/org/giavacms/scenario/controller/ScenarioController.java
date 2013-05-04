package org.giavacms.scenario.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.FileUtils;
import org.giavacms.base.common.util.ImageUtils;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.model.Search;
import org.giavacms.scenario.model.Scenario;
import org.giavacms.scenario.model.ScenarioConfiguration;
import org.giavacms.scenario.pojo.ProductDataModel;
import org.giavacms.scenario.repository.ScenarioConfigurationRepository;
import org.giavacms.scenario.repository.ScenarioRepository;
import org.primefaces.event.FileUploadEvent;


@Named
@SessionScoped
public class ScenarioController extends AbstractLazyController<Scenario> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/scenario/view.xhtml";
	@ListPage
	public static String LIST = "/private/scenario/list.xhtml";
	@EditPage
	public static String EDIT1 = "/private/scenario/edit1.xhtml";

	public static String EDIT2 = "/private/scenario/edit2.xhtml";

	// ------------------------------------------------

	@Override
	public void defaultCriteria()
	{
	   getSearch().getObj().setTemplate(new TemplateImpl());
	}

	@Override
	public Object getId(Scenario t)
	{
	   return t.getId();
	}
	
	@Inject
	@OwnRepository(ScenarioRepository.class)
	ScenarioRepository scenarioRepository;

	@Inject
	ProductRepository productRepository;

	@Inject
	ScenarioConfigurationRepository scenarioConfigurationRepository;

	private ProductDataModel products;
	private Product[] selectedProducts;

	// --------------------------------------------------------

	public void handleFileUpload(FileUploadEvent event) {
		logger.info("Uploaded: {}" + event.getFile().getFileName());
		logger.info("Uploaded: {}" + event.getFile().getContentType());
		logger.info("Uploaded: {}" + event.getFile().getSize());
		Document doc = new Document();
		doc.setUploadedData(event.getFile());
		doc.setData(event.getFile().getContents());
		doc.setType(event.getFile().getContentType());
		String filename = FileUtils.createFile_("docs", event.getFile()
				.getFileName(), event.getFile().getContents());
		doc.setFilename(filename);
		getElement().getDocuments().add(doc);
	}

	public void handleImgUpload(FileUploadEvent event) {
		logger.info("Uploaded: {}" + event.getFile().getFileName());
		logger.info("Uploaded: {}" + event.getFile().getContentType());
		logger.info("Uploaded: {}" + event.getFile().getSize());
		try {
			String type = event
					.getFile()
					.getFileName()
					.substring(
							event.getFile().getFileName().lastIndexOf(".") + 1);

			ScenarioConfiguration scenarioConfiguration = scenarioConfigurationRepository
					.load();
			byte[] imgRes;
			if (scenarioConfiguration.isResize()) {
				imgRes = ImageUtils.resizeImage(event.getFile().getContents(),
						scenarioConfiguration.getMaxWidthOrHeight(), type);
			} else {
				imgRes = event.getFile().getContents();
			}
			Image img = new Image();
			img.setUploadedData(event.getFile());
			img.setData(imgRes);
			img.setType(event.getFile().getContentType());
			String filename = FileUtils.createImage_("img", event.getFile()
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
			scenarioRepository.update(getElement());
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
			scenarioRepository.update(getElement());
		} else
			logger.info("removeImage: non posso rimuovere id:" + id);
	}

	// --------------------------------------------------------

	public String goToStep2() {
		Product productS = new Product();
		Search<Product> search = new Search<Product>(productS);
		this.products = new ProductDataModel(productRepository.getList(search,
				0, 0));
		// RECUPERO QUELLI GIA' SELEIONATI
		List<Product> selectedProductsArray = new ArrayList<Product>();
		for (Product product : this.products) {
			for (Product productItem : getElement().getProducts()) {
				if (productItem.getId().equals(product.getId())) {
					selectedProductsArray.add(productItem);
				}
			}
		}
		this.selectedProducts = selectedProductsArray.toArray(new Product[] {});
		return EDIT2 + REDIRECT_PARAM;
	}

	public void updateSelectedProducts() {
		// evito doppioni, almeno... beccare le deselezioni ancora non ho capito
		// come..
		Map<String, Product> uSelectedProducts = new HashMap<String, Product>();
		if (this.selectedProducts != null) {
			for (Product p : selectedProducts) {
				uSelectedProducts.put(p.getId(), p);
			}
		}
		this.selectedProducts = uSelectedProducts.values().toArray(
				new Product[] {});
	}

	@Override
	public String save() {
		// salvo l'associazione coi prodotti
		getElement().setProducts(null);
		for (Product product : this.selectedProducts) {
			getElement().addProduct(product);
		}
		// alla fine salvo lo scenario nuovo
		scenarioRepository.persist(getElement());
		refreshModel();
		return super.viewPage();
	}

	@Override
	public String delete() {
		return super.delete();
	}

	@Override
	public String update() {
		getElement().setProducts(null);
		scenarioRepository.update(getElement());
		if (this.selectedProducts != null && this.selectedProducts.length > 0) {
			for (Product product : this.selectedProducts) {
				getElement().addProduct(product);
			}
		}
		super.update();

		return super.viewPage();
	}

	public ProductDataModel getProducts() {
		return products;
	}

	public void setProducts(ProductDataModel products) {
		this.products = products;
	}

	public Product[] getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(Product[] selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

   public String getExtension()
   {
      return Scenario.EXTENSION;
   }


}
