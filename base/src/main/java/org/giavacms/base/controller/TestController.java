/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.request.PageRequestController;
import org.giavacms.common.util.JSFUtils;


@Named
@SessionScoped
public class TestController implements Serializable
{

   @Inject
   PageRequestController pageRequestController;

   private static final long serialVersionUID = 1L;

   public TestController()
   {
      System.out.println("testcontroller");
   }

   private String code;

   public String getCode()
   {
      return code;
   }

   public void setCode(String code)
   {
      this.code = code;
   }

   public void save()
   {
      System.out.println("code: " + this.code);
      try
      {
         JSFUtils.redirect("/d/dina2");
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      // pageRequestController.getElement().setId("dina2");
      // return "/page.xhtml?faces-redirect=true";
   }
}
