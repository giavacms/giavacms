package org.giavacms.insuranceclaim.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.insuranceclaim.model.InsuranceClaimConfiguration;
import org.giavacms.insuranceclaim.repository.InsuranceClaimConfigurationRepository;


@Named
@SessionScoped
public class InsuranceClaimConfigurationController extends
		AbstractLazyController<InsuranceClaimConfiguration> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/insuranceclaim/configuration.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(InsuranceClaimConfigurationRepository.class)
	InsuranceClaimConfigurationRepository insuranceClaimConfigurationRepository;

	@Override
	public InsuranceClaimConfiguration getElement() {
		if (super.getElement() == null)
			setElement(insuranceClaimConfigurationRepository.load());
		return super.getElement();
	}

}
