package org.giavacms.paypal.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.paypal.model.PaypalConfiguration;
import org.giavacms.paypal.repository.PaypalConfigurationRepository;


@Named
@SessionScoped
public class PaypalConfigurationController extends
		AbstractLazyController<PaypalConfiguration> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/paypal/configuration.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(PaypalConfigurationRepository.class)
	PaypalConfigurationRepository paypalConfigurationRepository;

	@Override
	public PaypalConfiguration getElement() {
		if (super.getElement() == null)
			setElement(paypalConfigurationRepository.load());
		return super.getElement();
	}
}
