package org.giavacms.catalogue.rest;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.giavacms.base.rest.RsMixinConfigurator;
import org.giavacms.catalogue.model.Product;
import org.jboss.logging.*;

@Singleton
public class CatalogueConfigurator {

	Logger logger = Logger.getLogger(getClass());
	@Inject
	RsMixinConfigurator rsMixinConfigurator;

	public CatalogueConfigurator() {
	}

	@PostConstruct
	public void init() {
		logger.info("add module");
		rsMixinConfigurator.getObjectMapper().getSerializationConfig()
				.addMixInAnnotations(Product.class, ProductMixin.class);

	}
}
