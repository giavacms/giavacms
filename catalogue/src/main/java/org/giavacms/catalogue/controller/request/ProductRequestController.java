package org.giavacms.catalogue.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageRequestController;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.model.Search;

@Named
@RequestScoped
public class ProductRequestController extends
         AbstractPageRequestController<Product> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam("categoria")
   String category;
   
   @Inject
   @HttpParam("q")
   String content;
   
   @Inject
   @HttpParam("t")
   String type;

   public static final String CURRENT_PAGE_PARAM = "p";

   @Inject
   @OwnRepository(ProductRepository.class)
   ProductRepository productRepository;

   @Inject
   CategoryRepository categoryRepository;

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   @Override
   public Search<Product> getSearch()
   {
      super.getSearch().getObj().setTitle(content);
      super.getSearch().getObj().getCategory().setTitle(category);
      return super.getSearch();
   }

   public boolean isScheda()
   {
      return getElement() != null && getElement().getId() != null;
   }

   public List<String> getProductCategories()
   {
      Search<Category> r = new Search<Category>(Category.class);
      List<Category> list = categoryRepository.getList(r, 0, 0);
      List<String> l = new ArrayList<String>();
      for (Category rnt : list)
      {
         l.add(rnt.getTitle());
      }
      return l;
   }

   public List<Category> getAllCategories()
   {
      Search<Category> r = new Search<Category>(Category.class);
      List<Category> l = categoryRepository.getList(r, 0, 0);
      return l;
   }

   @Deprecated
   public String getProductCategoryOptionsHTML()
   {
      StringBuffer sb = new StringBuffer();
      Search<Category> r = new Search<Category>(Category.class);
      List<Category> list = categoryRepository.getList(r, 0, 0);
      for (Category pc : list)
      {
         sb.append("<option value=\"")
                  .append(pc.getTitle())
                  .append("\"")
                  .append(pc.getTitle().equals(category) ? " selected=\"selected\""
                           : "").append(">").append(pc.getTitle())
                  .append("</option>");
      }
      return sb.toString();
   }

   @Override
   protected void handleI18N()
   {
      // TODO
   }
}
