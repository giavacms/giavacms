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
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.model.Search;

@Named
@RequestScoped
public class ProductRequestController extends
         AbstractPageRequestController<Product> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String PARAM_CATEGORY = "categoria";
   public static final String PARAM_CONTENT = "q";
   public static final String PARAM_TYPE = "t";
   public static final String CURRENT_PAGE_PARAM = "p";
   public static final String[] PARAM_NAMES = new String[] { PARAM_CONTENT,
            PARAM_TYPE,
            PARAM_CATEGORY,
            CURRENT_PAGE_PARAM };

   @Inject
   @OwnRepository(ProductRepository.class)
   ProductRepository productRepository;

   @Inject
   CategoryRepository categoryRepository;

   public ProductRequestController()
   {
      super();
   }

   @Override
   public void initParameters()
   {
      super.initParameters();
   }

   @Override
   public String[] getParamNames()
   {
      return PARAM_NAMES;
   }

   @Override
   public List<Product> loadPage(int startRow, int pageSize)
   {
      Search<Product> r = new Search<Product>(Product.class);
      r.getObj().setTitle(getParams().get(PARAM_CONTENT));
      r.getObj().getCategory().setTitle(getParams().get(PARAM_CATEGORY));
      return productRepository.getList(r, startRow, pageSize);
   }

   @Override
   public int totalSize()
   {
      // siamo all'interno della stessa richiesta per servire la quale è
      // avvenuta la postconstruct
      Search<Product> r = new Search<Product>(Product.class);
      r.getObj().getCategory().setTitle(getParams().get(PARAM_CATEGORY));
      r.getObj().setTitle(getParams().get(PARAM_CONTENT));
      return productRepository.getListSize(r);
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
                  .append(pc.getTitle().equals(getParams().get(PARAM_CATEGORY)) ? " selected=\"selected\""
                           : "").append(">").append(pc.getTitle())
                  .append("</option>");
      }
      return sb.toString();
   }

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

}
