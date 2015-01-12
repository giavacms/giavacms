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
import org.giavacms.insuranceclaim.model.InsuranceClaimTypology;
import org.giavacms.insuranceclaim.producer.InsuranceClaimProducer;
import org.giavacms.insuranceclaim.repository.InsuranceClaimTypologyRepository;


@Named
@SessionScoped
public class InsuranceClaimTypologyController extends
		AbstractLazyController<InsuranceClaimTypology> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/insuranceclaim/typology/list.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(InsuranceClaimTypologyRepository.class)
	InsuranceClaimTypologyRepository insuranceClaimTypologyRepository;

	@Inject
	InsuranceClaimProducer insuranceClaimProducer;

	// --------------------------------------------------------

	@Override
	public void initController() {
		if (getElement() == null) {
			setElement(new InsuranceClaimTypology());
		}
	}

	@Override
	public String save() {
		super.save();
		insuranceClaimProducer.reset();
		setElement(new InsuranceClaimTypology());
		return listPage();
	}

	@Override
	public String delete() {
		insuranceClaimProducer.reset();
		return listPage();
	}

	@Override
	public String update() {
		super.update();
		insuranceClaimProducer.reset();
		return listPage();
	}

}
