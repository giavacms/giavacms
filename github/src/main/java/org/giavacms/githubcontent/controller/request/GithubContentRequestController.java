package org.giavacms.githubcontent.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageRequestController;
import org.giavacms.base.pojo.I18nRequestParams;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.model.Group;
import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richcontent.repository.TagRepository;

@Named
@RequestScoped
public class GithubContentRequestController extends
		AbstractPageRequestController<RichContent> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_CONTENT = "q";
	public static final String PARAM_TYPE = "t";
	public static final String PARAM_TAG = "tag";
	public static final String CURRENT_PAGE_PARAM = "p";
	public static final String[] PARAM_NAMES = new String[] { PARAM_CONTENT,
			PARAM_TYPE, PARAM_TAG, CURRENT_PAGE_PARAM };

	@Inject
	@OwnRepository(RichContentRepository.class)
	RichContentRepository richContentRepository;

	@Inject
	RichContentTypeRepository richContentTypeRepository;

	private RichContent last;

	@Inject
	TagRepository tagRepository;

	public GithubContentRequestController() {
		super();
	}

	@Override
	public void initParameters() {
		super.initParameters();
		this.handleI18N();
	}

	@Override
	public String[] getParamNames() {
		return PARAM_NAMES;
	}

	public List<String> getTipiRichContent() {
		Search<RichContentType> r = new Search<RichContentType>(
				RichContentType.class);
		List<RichContentType> rntl = richContentTypeRepository.getList(r, 0, 0);
		List<String> l = new ArrayList<String>();
		for (RichContentType rnt : rntl) {
			l.add(rnt.getName());
		}
		return l;
	}

	@Override
	public String getCurrentPageParam() {
		return CURRENT_PAGE_PARAM;
	}

	public boolean isScheda() {
		return getElement() != null && getElement().getId() != null;
	}

	public RichContent getLast(String category) {
		if (last == null) {
			RichContent last = richContentRepository.getLast(category);
			this.last = last;
		}
		return last;
	}

	protected void handleI18N() {

		String testName = "test";
		I18nRequestParams i18nRequestParams = super.getI18nRequestParams();

		int currentLang = getBasePage().getLang();

		String testValue = i18nRequestParams.get(currentLang, testName);
		if (testValue != null && testValue.trim().length() > 0) {
			for (int i = 0; i < i18nRequestParams.getLanguages().length; i++) {
				if (i == currentLang) {
					continue;
				}
				i18nRequestParams
						.put(i,
								testName,
								(i18nRequestParams.getLanguages()[i] == null || !i18nRequestParams
										.getLanguages()[i].isEnabled()) ? "UNSUPPORTED"
										: (testValue + "_" + i18nRequestParams
												.getLanguages()[i].getId()));
			}
		}
	}

	@Produces
	@Named
	public List<Group<Tag>> getRequestTags() {
		Search<Tag> st = new Search<Tag>(Tag.class);
		st.setGrouping("tagName");
		st.getObj().setRichContent(new RichContent());
		st.getObj().getRichContent()
				.setRichContentType(getSearch().getObj().getRichContentType());
		return tagRepository.getGroups(st, 0, 10);

	}

	@Override
	public List<RichContent> loadPage(int startRow, int pageSize) {
		List<RichContent> page = super.loadPage(startRow, pageSize);
		if (page != null) {
			for (RichContent richContent : page) {
				richContentRepository.loadFirstPicture(richContent);
			}
		}
		return page;
	}

}
