package org.giavacms.picasa.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.picasa.model.PicasaConfiguration;
import org.giavacms.picasa.repository.PicasaConfigurationRepository;


@Named
@SessionScoped
public class PicasaConfigurationController extends
		AbstractLazyController<PicasaConfiguration> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/picasa/configuration.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(PicasaConfigurationRepository.class)
	PicasaConfigurationRepository picasaConfigurationRepository;

	@Override
	public PicasaConfiguration getElement() {
		if (super.getElement() == null)
			setElement(picasaConfigurationRepository.load());
		return super.getElement();
	}

}
