package org.giavacms.base.rest;

import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

@Provider
public class RsMixinConfigurator extends ResteasyJacksonProvider {

	private static final Logger log = Logger
			.getLogger(RsMixinConfigurator.class.getName());

	public RsMixinConfigurator() {
		super();
		// ObjectMapper mapper = _mapperConfig.getConfiguredMapper();
		// configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
		// false);
		// configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

	}

	public ObjectMapper getObjectMapper() {
		return _mapperConfig.getConfiguredMapper();
	}
}
