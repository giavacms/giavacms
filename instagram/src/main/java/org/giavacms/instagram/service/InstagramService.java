package org.giavacms.instagram.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.instagram.api.model.result.AccessTokenResult;
import org.giavacms.instagram.api.model.result.PaginationResult;
import org.giavacms.instagram.api.model.result.TagCountResult;
import org.giavacms.instagram.api.model.result.UserSearchResult;
import org.giavacms.instagram.api.search.SearchAccessTokenByCode;
import org.giavacms.instagram.api.search.SearchMediaByTag;
import org.giavacms.instagram.api.search.SearchMediaCountByTag;
import org.giavacms.instagram.api.search.SearchUserByNickname;
import org.giavacms.instagram.api.search.SearchUserMediaById;
import org.giavacms.instagram.repository.InstagramConfigurationRepository;


@Stateless
@LocalBean
public class InstagramService {

	@Inject
	InstagramConfigurationRepository instagramConfigurationRepository;

	public int getMediaByTagSize(String tag) throws Exception {
		TagCountResult result = SearchMediaCountByTag.execute(
				instagramConfigurationRepository.load().getToken(), tag, true);
		return result.getData().getMediaCount();
	}

	public PaginationResult getMediaByTag(String tag, String maxTagId)
			throws Exception {
		PaginationResult result = SearchMediaByTag.execute(
				instagramConfigurationRepository.load().getToken(), maxTagId,
				tag, true);
		return result;
	}

	public UserSearchResult getUserByNickname(String nickname) throws Exception {
		UserSearchResult result = SearchUserByNickname.execute(
				instagramConfigurationRepository.load().getToken(), nickname,
				1000, true);
		return result;
	}

	public PaginationResult getUserMediaById(String userId, String maxTagId)
			throws Exception {
		PaginationResult result = SearchUserMediaById.execute(
				instagramConfigurationRepository.load().getToken(), maxTagId,
				userId, true);
		return result;
	}

	public AccessTokenResult getAccessTokenByCode() throws Exception {
		AccessTokenResult result = SearchAccessTokenByCode.execute(
				instagramConfigurationRepository.load().getClientId(),
				instagramConfigurationRepository.load().getClientSecret(),
				instagramConfigurationRepository.load().getGrantType(),
				instagramConfigurationRepository.load().getRedirectUri(),
				instagramConfigurationRepository.load().getCode(), true);
		return result;
	}

}
