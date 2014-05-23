package org.giavacms.base.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.giavacms.base.rest.producer.ObjectMapperProducer;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

	private ObjectMapper _objectMapper;

	Logger logger = Logger.getLogger(JacksonContextResolver.class.getName());

	public JacksonContextResolver() {
		logger.info("JacksonContextResolver startup");
		_objectMapper = new ObjectMapperProducer().getObjectMapper();
		_objectMapper
				.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
	}

	public ObjectMapper getContext(Class<?> objectType) {
		logger.info(_objectMapper.toString());
		return _objectMapper;
	}
}