package org.giavacms.richcontent.service.rs.util;

import java.util.ArrayList;
import java.util.List;

import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.service.rs.json.JRichContent;
import org.giavacms.richcontent.service.rs.json.JRichContentType;

public class JsonUtils {

	public static JRichContent getJRichContent(RichContent richContent) {
		JRichContent jRichContent = new JRichContent(richContent.getId(),
				richContent.getTitle(), richContent.getDescription(),
				richContent.getPreview(), richContent.getContent(),
				richContent.getAuthor(), richContent.getDate(),
				getJRichContentType(richContent.getRichContentType()),
				richContent.getDocuments(), richContent.getImages(),
				richContent.isHighlight(), richContent.getTagList());
		return jRichContent;
	}

	public static List<JRichContent> getJRichContents(
			List<RichContent> richContents) {
		List<JRichContent> jproducts = new ArrayList<JRichContent>();
		for (RichContent richContent : richContents) {
			jproducts.add(getJRichContent(richContent));
		}
		return jproducts;

	}

	public static JRichContentType getJRichContentType(
			RichContentType richContentType) {
		JRichContentType jRichContentType = new JRichContentType(
				richContentType.getId(), richContentType.getName());
		return jRichContentType;
	}

	public static List<JRichContentType> getJRichContentTypes(
			List<RichContentType> richContentTypes) {
		List<JRichContentType> jcategories = new ArrayList<JRichContentType>();
		for (RichContentType richContentType : richContentTypes) {
			jcategories.add(getJRichContentType(richContentType));
		}
		return jcategories;

	}
}
