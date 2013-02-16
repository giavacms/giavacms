package org.giavacms.instagram.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.instagram.api.model.result.PaginationResult;
import org.giavacms.instagram.api.model.result.UserSearchResult;
import org.giavacms.instagram.model.InstagramCollection;
import org.giavacms.instagram.repository.InstagramCollectionRepository;
import org.giavacms.instagram.service.InstagramService;


@Named
@RequestScoped
public class InstagramRequestController extends
		AbstractRequestController<InstagramCollection> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TAG = "tag";
	public static final String USERID = "userid";
	public static final String USERNAME = "username";
	public static final String MAXTAGID = "maxTagId";
	public static final String Q = "q";
	public static final String TYPE = "type";
	public static final String ID_PARAM = "id";
	public static final String[] PARAM_NAMES = new String[] { TAG, USERID,
			USERNAME, MAXTAGID, Q, TYPE, ID_PARAM };

	@Inject
	InstagramService instagramService;

	@Inject
	@OwnRepository(InstagramCollectionRepository.class)
	InstagramCollectionRepository instagramCollectionRepository;

	private PaginationResult mediaByTag;
	private int mediaByTagSize;
	private UserSearchResult userByNickname;
	private PaginationResult userMediaById;

	public InstagramRequestController() {
		super();
	}

	@Override
	protected void init() {
		super.init();
	}

	public int getMediaByTagSize() {
		if (mediaByTagSize != 0)
			return mediaByTagSize;
		try {
			if (JSFUtils.getParameter(TAG) == null)
				return 0;
			mediaByTagSize = instagramService.getMediaByTagSize(JSFUtils
					.getParameter(TAG).toString());
			return mediaByTagSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public PaginationResult getMediaByTag() {
		if (mediaByTag != null)
			return mediaByTag;
		try {
			if (JSFUtils.getParameter(TAG) == null)
				return null;
			Object tag = JSFUtils.getParameter(TAG);
			Object maxTagId = JSFUtils.getParameter(MAXTAGID);
			mediaByTag = instagramService.getMediaByTag(
					tag != null ? (String) tag : "",
					maxTagId != null ? (String) maxTagId : "");
			return mediaByTag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserSearchResult getUserByNickname() {
		if (userByNickname != null)
			return userByNickname;
		try {
			if (JSFUtils.getParameter(Q) == null)
				return null;
			Object q = JSFUtils.getParameter(Q);
			userByNickname = instagramService
					.getUserByNickname(q != null ? (String) q : "");
			return userByNickname;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PaginationResult getUserMediaById() {
		if (userMediaById != null)
			return userMediaById;
		try {
			if (JSFUtils.getParameter(USERID) == null)
				return null;
			Object userId = JSFUtils.getParameter(USERID);
			Object maxTagId = JSFUtils.getParameter(MAXTAGID);
			userMediaById = instagramService.getUserMediaById(
					userId != null ? (String) userId : "",
					maxTagId != null ? (String) maxTagId : "");
			return userMediaById;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int totalSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCurrentPageParam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getParamNames() {
		return PARAM_NAMES;
	}

	@Override
	protected String getIdParam() {
		return ID_PARAM;
	}

	@Override
	protected List<InstagramCollection> loadPage(int startRow, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
