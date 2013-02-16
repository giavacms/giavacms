package org.giavacms.contactus.controller.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.contactus.model.ContactUs;
import org.giavacms.contactus.repository.ContactUsRepository;

/**
 * 
 * <form action="" method="post"> <h:outputText
 * value="#{contactUsRequestController.returnMessage}"/> <br/>
 * Nome: <input name="name" type="text"/> <br/>
 * Email: <input name="email" type="text"/> <br/>
 * Message: <textarea name="message" cols="40" rows="6"></textarea> <input
 * type="submit" name="submit" value="invia"/> </form>
 * 
 * @author pisi79
 * 
 */
@Named
@RequestScoped
public class ContactUsRequestController extends
		AbstractRequestController<ContactUs> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_NAME = "name";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_MESSAGE = "message";
	public static final String[] PARAM_NAMES = new String[] { PARAM_NAME,
			PARAM_EMAIL, PARAM_MESSAGE };
	public static final String ID_PARAM = "unused_idParam";
	public static final String CURRENT_PAGE_PARAM = "unused_currentpage";

	@Inject
	@OwnRepository(ContactUsRepository.class)
	ContactUsRepository contactUsRepository;

	public ContactUsRequestController() {
		super();
	}

	@Override
	public List<ContactUs> loadPage(int startRow, int pageSize) {
		return null;
	}

	@Override
	public int totalSize() {
		return 0;
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
		if (params.get(PARAM_MESSAGE) != null) {
			ContactUs contactUs = new ContactUs();
			contactUs.setData(new Date());
			contactUs.setEmail(params.get(PARAM_EMAIL));
			contactUs.setName(params.get(PARAM_NAME));
			contactUs.setMessage(params.get(PARAM_MESSAGE));
			contactUsRepository.persist(contactUs);
			return "Grazie per averci contattato";
		} else {
			return null;
		}
	}

}
