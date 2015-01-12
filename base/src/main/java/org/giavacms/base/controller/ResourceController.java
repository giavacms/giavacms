/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;
import org.giavacms.base.annotation.DefaultResourceController;
import org.giavacms.base.common.util.ImageUtils;
import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.pojo.Resource;
import org.giavacms.base.producer.BaseProducer;
import org.giavacms.base.repository.ResourceRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.util.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@DefaultResourceController
public class ResourceController extends AbstractLazyController<Resource>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ListPage
	@ViewPage
	public static String LIST = "/private/resource/list.xhtml";
	@EditPage
	public static String EDIT = "/private/resource/edit.xhtml";

	public static String UPLOAD = "/private/resource/load.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(ResourceRepository.class)
	ResourceRepository resourceRepository;

	@Inject
	BaseProducer baseProducer;

	// --------------------------------------------------------

	private List<Resource> uploadedResources = null;

	private UploadedFile replacementFile;

	int width, height;
	private CroppedImage croppedImage;

	// ------------------------------------------------

	@Override
	public void initController() {
		for (ResourceType resourceType : ResourceType.values()) {
			if (resourceType.getFolder().isEmpty())
				continue;
			File d = new File(ResourceUtils.getRealPath()
					+ resourceType.getFolder());
			if (!d.exists()) {
				if (!d.mkdir()) {
					logger.error("errors creating path:"
							+ resourceType.getFolder());
				} else {
					logger.info("the folder:" + resourceType.getFolder()
							+ " created!");
				}
			} else if (d.exists() && !d.isDirectory()) {
				logger.error("path could not be created:"
						+ resourceType.getFolder());
			} else {
				logger.info("the path:" + resourceType.getFolder() + " exists!");
			}
		}
	}

	/**
	 * Obbligatoria l'invocazione 'appropriata' di questo super construttore
	 * protetto da parte delle sottoclassi
	 */
	public ResourceController() {
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public CroppedImage getCroppedImage() {
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}

	// ------------------------------------------------

	/**
	 * Metodo da implementare per assicurare che i criteri di ricerca contengano
	 * sempre tutti i vincoli desiderati (es: identità utente, selezioni
	 * esterne, ecc...)
	 */
	@Override
	public void defaultCriteria() {
		getSearch().getObj().setType(
				baseProducer.getResourceItems()[0].getValue() + "");
	}

	/**
	 * Metodo di navigazione per resettare lo stato interno e tornare alla
	 * pagina dell'elenco generale
	 * 
	 * @return
	 */
	@Override
	public String reset() {
		this.uploadedResources = null;
		return super.reset();
	}

	// -----------------------------------------------------

	@Override
	public String addElement() {
		// impostazioni locali
		this.uploadedResources = new ArrayList<Resource>();
		super.addElement();
		return UPLOAD + REDIRECT_PARAM;
	}

	@Override
	public String modElement() {
		Resource t = (Resource) getModel().getRowData();
		t = resourceRepository.fetch(t.getType(), t.getId());
		super.setEditMode(true);
		super.setReadOnlyMode(false);
		setElement(t);
		setDimensions();
		return editPage();
	}

	@Override
	public String modCurrent() {
		Resource t = resourceRepository.fetch(getElement().getType(),
				getElement().getId());
		super.setEditMode(true);
		super.setReadOnlyMode(false);
		setElement(t);
		setDimensions();
		return editPage();
	}

	@Override
	public String save() {
		for (Resource resource : uploadedResources) {
			if (resource.getType() == null)
				resource.setType(getElement().getType());
			resourceRepository.persist(resource);
		}
		// refresh locale
		refreshModel();
		// altre dipendenze
		//
		return listPage();
	}

	@Override
	public String update() {
		if (ResourceType.JAVASCRIPT.equals(getElement().getResourceType())
				|| ResourceType.STYLESHEET.equals(getElement()
						.getResourceType())
				|| ResourceType.STATIC.equals(getElement().getResourceType())) {
			logger.debug("don't exist new resource uploaded; let's persist modifications on text");
			resourceRepository.updateResource(getElement());
			setElement(resourceRepository.fetch(getElement().getType(),
					getElement().getId()));
			// refresh locale
			refreshModel();
		} else {
			logger.debug("we need to cope with replacement via resource uploaded");

			if (getReplacementFile() != null) {
				try {
					String filename = getReplacementFile().getFileName();
					if (filename.contains("\\"))
						filename = filename.substring(filename
								.lastIndexOf("\\") + 1);
					Resource resource = new Resource();
					resource.setInputStream(getReplacementFile()
							.getInputstream());
					resource.setBytes(getReplacementFile().getContents());
					resource.setName(filename);
					resource.setId(getElement().getId());
					resource.setResourceType(ResourceUtils
							.getResourceType(filename));
					resourceRepository.updateResource(resource);
					setElement(resource);
					// refresh locale
					refreshModel();
				} catch (IOException e) {
					logger.info(e.getMessage());
					super.addFacesMessage("Errori nel salvataggio della risorsa");
					return null;
				}
			}
		}
		return viewPage();
	}

	public String delElement(String tipo, String id) {
		logger.info("delElement: " + tipo + " -" + id);
		Resource resource = new Resource();
		resource.setId(id);
		resource.setName(id);
		resource.setType(tipo);
		resourceRepository.delete(resource);
		return listPage();
	}

	// -----------------------------------------------------

	public void handleFileUpload(FileUploadEvent event) {
		try {
			UploadedFile file = event.getFile();
			InputStream is = event.getFile().getInputstream();
			String filename = file.getFileName();
			if (filename.contains("\\"))
				filename = filename.substring(filename.lastIndexOf("\\") + 1);
			byte[] bites = IOUtils.toByteArray(is);
			Resource resource = new Resource();
			resource.setInputStream(is);
			resource.setName(filename);
			resource.setType(ResourceUtils.getType(filename));

			resource.setBytes(bites);
			uploadedResources.add(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------
	public List<Resource> getUploadedResources() {
		return uploadedResources;
	}

	public Resource getResource(int index) {
		return uploadedResources.get(index);
	}

	public StreamedContent getResourceStream(int index) {
		Resource rs = uploadedResources.get(index);
		if (!ResourceType.IMAGE.equals(rs.getResourceType()))
			return null;
		StreamedContent image = new DefaultStreamedContent(rs.getInputStream(),
				rs.getType());
		return image;
	}

	public Resource getSingleResource(int row) {
		try {
			Resource resource = uploadedResources.get(row);
			return resource;
		} catch (Exception e) {
			return null;
		}
	}

	public String getExtensionsByType(String type) {
		return ResourceUtils.getRegExpByTypes(new String[] { type });
	}

	public UploadedFile getReplacementFile() {
		return replacementFile;
	}

	public void setReplacementFile(UploadedFile replacementFile) {
		this.replacementFile = replacementFile;
	}

	// --------------------------------------------------------

	protected void setDimensions() {
		width = 0;
		height = 0;
		croppedImage = null;
		if (!ResourceType.IMAGE.equals(getElement().getResourceType())) {
			return;
		}
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String folder = servletContext.getRealPath("") + File.separator;
		getElement().setBytes(
				FileUtils
						.getBytesFromFile(new File(new File(folder,
								ResourceType.IMAGE.getFolder()), getElement()
								.getName())));
		ImageIcon imageIcon = new ImageIcon(getElement().getBytes());
		width = imageIcon.getIconWidth();
		height = imageIcon.getIconHeight();
	}

	public String resize() {
		try {
			if (!resourceRepository.createSubFolder(ResourceType.IMAGE,
					"resized")) {
				super.addFacesMessage("Errore durante la scrittura dei dati temporanei");
				return null;
			}
			byte[] resized = null;
			if (width == 0 || height == 0) {
				resized = ImageUtils.resizeImage(getElement().getBytes(),
						width == 0 ? height : width,
						FileUtils.getExtension(getElement().getName()));
			} else {
				resized = ImageUtils.resizeImage(getElement().getBytes(),
						width, height,
						FileUtils.getExtension(getElement().getName()));
			}
			getElement().setBytes(resized);
			getElement().setType(getElement().getType() + "/resized");
			resourceRepository.updateResource(getElement());
			return modCurrent();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.addFacesMessage("Errori nel ridimensionamento dell'immagine");
			return null;
		}
	}

	public String crop() {
		if (croppedImage == null)
			return null;

		ByteArrayOutputStream baos = null;
		try {
			if (!resourceRepository.createSubFolder(ResourceType.IMAGE,
					"cropped")) {
				super.addFacesMessage("Errore durante la scrittura dei dati temporanei");
				return null;
			}
			baos = new ByteArrayOutputStream();
			baos.write(croppedImage.getBytes(), 0,
					croppedImage.getBytes().length);
			getElement().setBytes(baos.toByteArray());
			getElement().setType(getElement().getType() + "/cropped");
			resourceRepository.updateResource(getElement());
			return modCurrent();
		} catch (Exception e) {
			super.addFacesMessage("Errori nel ritaglio dell'immagine");
			logger.error(e.getMessage(), e);
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	public String undo() {
		resourceRepository.delete(getElement());
		getElement().setType(ResourceType.IMAGE.getFolder());
		return modCurrent();
	}

	public String confirm() {
		resourceRepository.delete(getElement());
		getElement().setType(ResourceType.IMAGE.getFolder());
		resourceRepository.updateResource(getElement());
		return modCurrent();
	}

	public String copy() {
		resourceRepository.delete(getElement());
		getElement().setType(ResourceType.IMAGE.getFolder());
		resourceRepository.persist(getElement());
		getElement().setId(FileUtils.getLastPartOf(getElement().getId()));
		return modCurrent();
	}

	// --------------------------------

	public void substitute(FileUploadEvent event) {
		this.replacementFile = event.getFile();
		update();
	}

	// ------------------------------

	public void checkExists() {
		String folder = getSearch().getObj().getType();
		if (folder == null || folder.isEmpty()) {
			return;
		}
		String filename = getSearch().getObj().getName();
		if (filename == null || filename.isEmpty()) {
			return;
		}
		String extension = FileUtils.getExtension(filename);
		ResourceType resourceType = ResourceType.getValueByFolder(folder);
		if (extension == null
				|| !resourceType.getExtensions().contains(
						extension.toLowerCase())) {
			filename += "." + resourceType.getExtensions().get(0);
		}
		File file = new File(ResourceUtils.getRealPath() + "/"
				+ getSearch().getObj().getType(), filename);
		getSearch().getObj().setExists(file.exists());
	}

	public String createElement() {
		checkExists();
		if (getSearch().getObj().isExists()) {
			super.addFacesMessage("Risorsa gia' presente");
		}
		String folder = getSearch().getObj().getType();
		String filename = getSearch().getObj().getName();
		String extension = FileUtils.getExtension(filename);
		ResourceType resourceType = ResourceType.getValueByFolder(folder);
		if (extension == null
				|| !resourceType.getExtensions().contains(
						extension.toLowerCase())) {
			filename += "." + resourceType.getExtensions().get(0);
		}
		String content = "";
		switch (resourceType) {
		case STYLESHEET:
			content = "redStyle { color: red; }";
			break;
		case JAVASCRIPT:
			content = "var uno = 1;";
			break;
		case STATIC:
			content = "<!DOCTYPE HTML>";
			break;
		default:
			content = "...";
			break;
		}
		String file = ResourceUtils.createFile_(getSearch().getObj().getType(),
				filename, content.getBytes());
		Resource resource = resourceRepository.fetch(folder, file);
		setElement(resource);
		return modCurrent();
	}
}