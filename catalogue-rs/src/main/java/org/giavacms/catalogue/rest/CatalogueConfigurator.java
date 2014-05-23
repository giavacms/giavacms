package org.giavacms.catalogue.rest;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.giavacms.base.rest.RsMixinConfigurator;
import org.giavacms.catalogue.model.Product;

@Singleton
public class CatalogueConfigurator {

	@Inject
	RsMixinConfigurator rsMixinConfigurator;

	public CatalogueConfigurator() {
	}

	@PostConstruct
	public void init() {
		rsMixinConfigurator.getObjectMapper().getSerializationConfig()
				.addMixInAnnotations(Product.class, ProductMixin.class);

	}
}
