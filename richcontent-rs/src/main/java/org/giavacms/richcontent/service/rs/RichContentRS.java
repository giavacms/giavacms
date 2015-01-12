package org.giavacms.richcontent.service.rs;

/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

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
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.service.rs.json.JRichContent;
import org.giavacms.richcontent.service.rs.util.JsonUtils;
import org.jboss.logging.Logger;

@Path("/v1/richcontent")
@Stateless
@LocalBean
public class RichContentRS implements Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(getClass().getCanonicalName());

	@Inject
	RichContentRepository richContentRepository;

	@GET
	@Path("/richcontents")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JRichContent> getList(@QueryParam("startRow") int startRow,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("type") String type) {
		Search<RichContent> search = new Search<RichContent>(RichContent.class);
		if (type != null && !type.isEmpty()) {
			search.getObj().getRichContentType().setName(type);
		}
		List<RichContent> richcontents = richContentRepository.getList(search,
				pageSize, pageSize);
		return JsonUtils.getJRichContents(richcontents);

	}

	@GET
	@Path("/richcontents/size")
	@Produces(MediaType.APPLICATION_JSON)
	public int getListSize(@QueryParam("type") String type) {
		Search<RichContent> search = new Search<RichContent>(RichContent.class);
		if (type != null && !type.isEmpty()) {
			search.getObj().getRichContentType().setName(type);
		}
		return richContentRepository.getListSize(search);

	}

	@GET
	@Path("/richcontents/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JRichContent getRichContent(@PathParam("id") String id)
			throws Exception {
		RichContent richContent = richContentRepository.fetch(id);
		return JsonUtils.getJRichContent(richContent);
	}

	@GET
	@Path("/richcontents/highlight")
	@Produces(MediaType.APPLICATION_JSON)
	public JRichContent gethighlight(@QueryParam("type") String type)
			throws Exception {
		Search<RichContent> search = new Search<RichContent>(RichContent.class);
		search.getObj().setHighlight(true);
		RichContent richcontent = richContentRepository.getHighlight(type, 1);
		return JsonUtils.getJRichContent(richcontent);
	}

	@GET
	@Path("/richcontents/last")
	@Produces(MediaType.APPLICATION_JSON)
	public JRichContent getLast(@QueryParam("type") String type)
			throws Exception {
		Search<RichContent> search = new Search<RichContent>(RichContent.class);
		search.getObj().setHighlight(true);
		RichContent richcontent = richContentRepository.getHighlight(type, 1);
		return JsonUtils.getJRichContent(richcontent);
	}

}
