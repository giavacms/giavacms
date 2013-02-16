package org.giavacms.richnews.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.request.PageRequestController;
import org.giavacms.base.pojo.I18nParams;
import org.giavacms.base.request.I18nProducer;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.BeanUtils;
import org.giavacms.richnews.model.RichNews;
import org.giavacms.richnews.model.type.RichNewsType;
import org.giavacms.richnews.repository.RichNewsRepository;
import org.giavacms.richnews.repository.RichNewsTypeRepository;


@Named
@RequestScoped
public class RichNewsRequestController extends
		AbstractRequestController<RichNews> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_CONTENT = "q";
	public static final String PARAM_TYPE = "type";
	public static final String ID_PARAM = "id";
	public static final String CURRENT_PAGE_PARAM = "currentpage";
	public static final String[] PARAM_NAMES = new String[] { PARAM_CONTENT,
			PARAM_TYPE, ID_PARAM, CURRENT_PAGE_PARAM };

	@Inject
	@OwnRepository(RichNewsRepository.class)
	RichNewsRepository richNewsRepository;

	@Inject
	RichNewsTypeRepository richNewsTypeRepository;

	@Inject
	I18nParams i18nParams;

	private String filter;

	private RichNews last;

	public RichNewsRequestController() {
		super();
	}

	@Override
	protected void init() {
		super.init();
		this.testI18N();
	}

	@Override
	public List<RichNews> loadPage(int startRow, int pageSize) {
		Search<RichNews> r = new Search<RichNews>(RichNews.class);
		r.getObj().setTitle(getParams().get(PARAM_CONTENT));
		r.getObj().getRichNewsType().setName(getParams().get(PARAM_TYPE));
		if (getFilter() != null && !getFilter().isEmpty()) {
			r.getObj().getRichNewsType().setName(getFilter());
		}
		return richNewsRepository.getList(r, startRow, pageSize);
	}

	public List<RichNews> getLatest(int pageSize) {
		logger.info("getLatest:" + pageSize);
		Search<RichNews> r = new Search<RichNews>(RichNews.class);
		return richNewsRepository.getList(r, 0, pageSize);
	}

	public List<RichNews> getPageOfSizeWithCategory(int size, String category) {
		setFilter(category);
		setPageSize(size);
		return getPage();
	}

	@Override
	public int totalSize() {
		Search<RichNews> r = new Search<RichNews>(RichNews.class);
		r.getObj().getRichNewsType().setName(getParams().get(PARAM_TYPE));
		r.getObj().setTitle(getParams().get(PARAM_CONTENT));
		if (getFilter() != null && !getFilter().isEmpty()) {
			r.getObj().getRichNewsType().setName(getFilter());
		}
		return richNewsRepository.getListSize(r);
	}

	@Override
	public String[] getParamNames() {
		return PARAM_NAMES;
	}

	@Override
	public String getIdParam() {
		return ID_PARAM;
	}

	public List<String> getTipiRichNews() {
		Search<RichNewsType> r = new Search<RichNewsType>(RichNewsType.class);
		List<RichNewsType> rntl = richNewsTypeRepository.getList(r, 0, 0);
		List<String> l = new ArrayList<String>();
		for (RichNewsType rnt : rntl) {
			l.add(rnt.getName());
		}
		return l;
	}

	public String getRichNewsTypeOptionsHTML() {
		StringBuffer sb = new StringBuffer();
		Search<RichNewsType> r = new Search<RichNewsType>(RichNewsType.class);
		List<RichNewsType> rntl = richNewsTypeRepository.getList(r, 0, 0);
		for (RichNewsType richNewsType : rntl) {
			sb.append("<option value=\"")
					.append(richNewsType.getName())
					.append("\"")
					.append(richNewsType.getName().equals(
							getParams().get(PARAM_TYPE)) ? " selected=\"selected\""
							: "").append(">").append(richNewsType.getName())
					.append("</option>");
		}
		return sb.toString();
	}

	@Override
	public String getCurrentPageParam() {
		return CURRENT_PAGE_PARAM;
	}

	public boolean isScheda() {
		return getElement() != null && getElement().getId() != null;
	}

	public String viewElement(Long id) {
		setElement(richNewsRepository.fetch(id));
		return viewPage();
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public RichNews getLast(String category) {
		if (last == null) {
			RichNews last = richNewsRepository.getLast(category);
			this.last = last;
		}
		return last;
	}

	protected void testI18N() {

		if (i18nParams == null) {
			logger.info("Attenzione: ancora non funziona l'inject diretta degli I18nParams. Per ottenereli recuperiamo il loro producer dal contesto cdi");
			I18nProducer i18nProducer = BeanUtils.getBean(I18nProducer.class);
			i18nParams = i18nProducer.getI18nParams();
		} else {
			logger.info("Attenzione: parametri I18nParams gia' presenti. Ma se in precedenza ho stampato il messaggio dell'inject che non funziona vuol dire che sto passando due volte per questo metodo, CIOE' CHE STO CREANDO DUE VOLTE UN REQUEST CONTROLLER !?!?!");
		}

		String testName = "test";
		int currentLang = ((PageRequestController) BeanUtils
				.getBean(PageRequestController.class)).getElement().getLang();
		String testValue = i18nParams.get(currentLang, testName);
		if (testValue != null && testValue.trim().length() > 0) {
			for (int i = 0; i < i18nParams.getLanguages().length; i++) {
				if (i == currentLang) {
					continue;
				}
				i18nParams
						.put(i,
								testName,
								(i18nParams.getLanguages()[i] == null || !i18nParams
										.getLanguages()[i].isEnabled()) ? "UNSUPPORTED"
										: (testValue + "_" + i18nParams
												.getLanguages()[i].getId()));
			}
		}
	}
}
