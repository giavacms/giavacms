package org.giavacms.instagram.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.instagram.api.model.result.PaginationResult;
import org.giavacms.instagram.api.model.result.UserSearchResult;
import org.giavacms.instagram.model.InstagramCollection;
import org.giavacms.instagram.repository.InstagramCollectionRepository;
import org.giavacms.instagram.service.InstagramService;

@Named
@RequestScoped
public class InstagramRequestController extends
         AbstractRequestController<InstagramCollection> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("tag")
   String tag;

   @Inject
   @HttpParam("userid")
   String userid;

   @Inject
   @HttpParam("username")
   String username;

   @Inject
   @HttpParam("maxTagId")
   String maxTagId;

   @Inject
   @HttpParam("q")
   String q;

   @Inject
   @HttpParam("type")
   String type;

   @Inject
   InstagramService instagramService;

   @Inject
   @OwnRepository(InstagramCollectionRepository.class)
   InstagramCollectionRepository instagramCollectionRepository;

   private PaginationResult mediaByTag;
   private int mediaByTagSize;
   private UserSearchResult userByNickname;
   private PaginationResult userMediaById;

   public InstagramRequestController()
   {
      super();
   }

   public int getMediaByTagSize()
   {
      if (mediaByTagSize != 0)
         return mediaByTagSize;
      try
      {
         if (tag == null || tag.trim().isEmpty())
            return 0;
         mediaByTagSize = instagramService.getMediaByTagSize(tag.toString());
         return mediaByTagSize;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return 0;
   }

   public PaginationResult getMediaByTag()
   {
      if (mediaByTag != null)
         return mediaByTag;
      try
      {
         if (tag == null || tag.trim().isEmpty())
            return null;
         mediaByTag = instagramService.getMediaByTag(
                  tag != null ? (String) tag : "",
                  maxTagId != null ? (String) maxTagId : "");
         return mediaByTag;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public UserSearchResult getUserByNickname()
   {
      if (userByNickname != null)
         return userByNickname;
      try
      {
         if (q == null)
            return null;
         userByNickname = instagramService
                  .getUserByNickname(q != null ? (String) q : "");
         return userByNickname;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public PaginationResult getUserMediaById()
   {
      if (userMediaById != null)
         return userMediaById;
      try
      {
         if (userid == null)
            return null;
         userMediaById = instagramService.getUserMediaById(
                  userid != null ? (String) userid : "",
                  maxTagId != null ? (String) maxTagId : "");
         return userMediaById;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   @Override
   public String getCurrentPageParam()
   {
      return null;
   }

   @Override
   protected String getIdParam()
   {
      return null;
   }

}
