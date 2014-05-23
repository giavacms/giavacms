/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.richcontent.service.rs;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.service.rs.json.JRichContentType;
import org.giavacms.richcontent.service.rs.util.JsonUtils;
import org.jboss.logging.Logger;

@Path("/v1/richcontenttype")
@Stateless
@LocalBean
public class RichContentTypeRs implements Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(getClass().getCanonicalName());

	@Inject
	RichContentTypeRepository richContentTypeRepository;

	@GET
	@Path("/richcontenttypes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JRichContentType> getList(@QueryParam("startRow") int startRow,
			@QueryParam("pageSize") int pageSize) {
		Search<RichContentType> search = new Search<RichContentType>(
				RichContentType.class);
		List<RichContentType> richContentTypes = richContentTypeRepository
				.getList(search, pageSize, pageSize);
		return JsonUtils.getJRichContentTypes(richContentTypes);

	}

	@GET
	@Path("/richcontenttypes/size")
	@Produces(MediaType.APPLICATION_JSON)
	public int getListSize() {
		Search<RichContentType> search = new Search<RichContentType>(
				RichContentType.class);
		return richContentTypeRepository.getListSize(search);

	}

	@GET
	@Path("/categories/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JRichContentType getCategory(@PathParam("id") String id)
			throws Exception {
		RichContentType richContentType = richContentTypeRepository.fetch(id);
		return JsonUtils.getJRichContentType(richContentType);
	}

}
