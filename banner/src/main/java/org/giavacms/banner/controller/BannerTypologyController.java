package org.giavacms.banner.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.banner.model.BannerTypology;
import org.giavacms.banner.producer.BannerProducer;
import org.giavacms.banner.repository.BannerTypologyRepository;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;

@Named
@SessionScoped
public class BannerTypologyController extends
         AbstractLazyController<BannerTypology>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/banner/typology/list.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(BannerTypologyRepository.class)
   BannerTypologyRepository bannerTypologyRepository;

   @Inject
   BannerProducer bannerProducer;

   // --------------------------------------------------------

   @Override
   public void initController()
   {
      if (getElement() == null)
      {
         setElement(new BannerTypology());
      }
   }

   @Override
   public String save()
   {
      super.save();
      bannerProducer.reset();
      setElement(new BannerTypology());
      return listPage();
   }

   @Override
   public String delete()
   {
      bannerProducer.reset();
      return listPage();
   }

   @Override
   public String update()
   {
      super.update();
      bannerProducer.reset();
      return listPage();
   }

}
