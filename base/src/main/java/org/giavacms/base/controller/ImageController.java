/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.repository.ResourceRepository;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;


@Named
@SessionScoped
public class ImageController extends ResourceController
{

   private static final long serialVersionUID = 1L;

   private static final String IMAGE_TYPE = "img";

   // --------------------------------------------------------

   @ListPage
   @ViewPage
   @EditPage
   public static String EDIT = "/private/page/tinyImages.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(ResourceRepository.class)
   ResourceRepository resourceRepository;

   @Override
   public void defaultCriteria()
   {
      super.defaultCriteria();
      getSearch().getObj().setType(IMAGE_TYPE);
   }

}