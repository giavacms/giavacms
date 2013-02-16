package org.giavacms.instagram.controller;

import java.io.IOException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.instagram.api.model.result.AccessTokenResult;
import org.giavacms.instagram.model.InstagramConfiguration;
import org.giavacms.instagram.repository.InstagramConfigurationRepository;
import org.giavacms.instagram.service.InstagramService;


@Named
@SessionScoped
public class InstagramConfigurationController extends
		AbstractLazyController<InstagramConfiguration> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/instagram/configuration.xhtml";

	private String logOperation;
	// ------------------------------------------------

	@Inject
	@OwnRepository(InstagramConfigurationRepository.class)
	InstagramConfigurationRepository instagramConfigurationRepository;

	@Inject
	InstagramService instagramService;

	@Override
	public InstagramConfiguration getElement() {
		if (super.getElement() == null)
			setElement(instagramConfigurationRepository.load());
		return super.getElement();
	}

	public String getLogOperation() {
		return logOperation;
	}

	public void setLogOperation(String logOperation) {
		this.logOperation = logOperation;
	}

	public String requestCode() {
		logger.info("requestCode");
		// https://instagram.com/oauth/authorize/?client_id=6fd0d93b10a84c168ed99c495adba692&redirect_uri=http://www.withflower.in/p/instagram&response_type=code
		try {
			String url = getElement().getInitialUri() + "?client_id="
					+ getElement().getClientId() + "&redirect_uri="
					+ getElement().getRedirectUri() + "&response_type=code";
			logger.info(url);
			JSFUtils.redirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void requestToken() {
		try {
			AccessTokenResult result = instagramService.getAccessTokenByCode();
			if (result != null && result.getAccessToken() != null
					&& !result.getAccessToken().isEmpty()) {
				getElement().setToken(result.getAccessToken());
				instagramConfigurationRepository.update(getElement());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
