package org.giavacms.message.controller.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.message.model.Message;
import org.giavacms.message.repository.MessageConfigurationRepository;
import org.giavacms.message.repository.MessageRepository;

@Named
@RequestScoped
public class MessageRequestController extends
		AbstractRequestController<Message> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_KEY = "key";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_BODY = "body";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_NAME = "name";
	public static final String[] PARAM_NAMES = new String[] { PARAM_TYPE,
			PARAM_KEY, PARAM_BODY, PARAM_EMAIL, PARAM_NAME };
	public static final String ID_PARAM = "idMessage";
	public static final String CURRENT_PAGE_PARAM = "currentpage";

	@Inject
	@OwnRepository(MessageRepository.class)
	MessageRepository messageRepository;

	@Inject
	MessageConfigurationRepository messageConfigurationRepository;

	public MessageRequestController() {
		super();
	}

	@Override
	public List<Message> loadPage(int startRow, int pageSize) {
		return loadPageWithParams(startRow, pageSize, getParams()
				.get(PARAM_KEY), getParams().get(PARAM_TYPE));
	}

	public List<Message> loadPageWithParams(int startRow, int pageSize,
			String key, String type) {
		Search<Message> r = new Search<Message>(Message.class);
		r.getObj().setSourceKey(key);
		r.getObj().setSourceType(type);
		r.getObj().setActive(true);
		r.setOrder("date asc");
		return messageRepository.getList(r, startRow, pageSize);
	}

	@Override
	public int totalSize() {
		// siamo all'interno della stessa richiesta per servire la quale è
		// avvenuta la postconstruct
		Search<Message> r = new Search<Message>(Message.class);
		r.getObj().setSourceKey(getParams().get(PARAM_KEY));
		r.getObj().setSourceType(getParams().get(PARAM_TYPE));
		return messageRepository.getListSize(r);
	}

	@Override
	public String[] getParamNames() {
		return PARAM_NAMES;
	}

	@Override
	public String getIdParam() {
		return ID_PARAM;
	}

	@Override
	public String getCurrentPageParam() {
		return CURRENT_PAGE_PARAM;
	}

	public String getReturnMessage() {
		if (params.get(PARAM_BODY) != null && params.get(PARAM_NAME) != null) {
			Message message = new Message();
			message.setDate(new Date());
			message.setEmail(params.get(PARAM_EMAIL));
			message.setName(params.get(PARAM_NAME));
			message.setBody(params.get(PARAM_BODY));
			message.setSourceKey(params.get(PARAM_KEY));
			message.setSourceType(params.get(PARAM_TYPE));
			if (!messageConfigurationRepository.load().isApprove()) {
				message.setActive(true);
			}
			messageRepository.persist(message);
			return "Grazie per il tuo commento!";
		} else {
			return null;
		}
	}

}
