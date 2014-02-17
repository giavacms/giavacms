/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.annotation.DefaultResourceController;
import org.giavacms.base.controller.AbstractPageWithImagesAndDocumentsController;
import org.giavacms.base.controller.ResourceController;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.controller.ProductController;
import org.giavacms.catalogue.model.Product;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.model.Search;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.model.CustomerToProduct;
import org.giavacms.customer.repository.CustomerCategoryRepository;
import org.giavacms.customer.repository.CustomerConfigurationRepository;
import org.giavacms.customer.repository.CustomerRepository;
import org.giavacms.customer.repository.CustomerToProductRepository;

@Named
@SessionScoped
public class CustomerController extends AbstractPageWithImagesAndDocumentsController<Customer>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/customer/view.xhtml";
   @ListPage
   public static String LIST = "/private/customer/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/customer/edit.xhtml";

   private static final String EDIT_DOCS = "/private/customer/edit-documents.xhtml";
   private static final String EDIT_PRODS = "/private/customer/edit-products.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CustomerRepository.class)
   CustomerRepository customerRepository;

   @Inject
   CustomerCategoryRepository customerCategoryRepository;

   @Inject
   CustomerConfigurationRepository customerConfigurationRepository;

   @Inject
   @DefaultResourceController
   ResourceController resourceController;

   @Inject
   CustomerToProductRepository customerToProductRepository;

   @Inject
   ProductController productController;

   // --------------------------------------------------------

   public CustomerController()
   {
   }

   @Override
   public String getExtension()
   {
      return Customer.EXTENSION;
   }

   // --------------------------------------------------------

   @Override
   public String save()
   {
      // implementare questo se la categoria determina la pagina base come rich content type fa con rich content
      // CustomerCategory customerCategory = customerCategoryRepository.find(getElement().getCategory().getId());
      // getElement().setTemplateId(customerCategory.getPage().getTemplate().getId());
      if (super.save() == null)
      {
         return null;
      }
      return super.viewPage();
   }

   @Override
   public String update()
   {
      // implementare questo se la categoria determina la pagina base come rich content type fa con rich content
      // CustomerCategory customerCategory = customerCategoryRepository.find(getElement().getCategory().getId());
      // getElement().setTemplateId(customerCategory
      // .getPage().getTemplateId());
      if (super.update() == null)
      {
         return null;
      }
      return super.viewPage();
   }

   @Override
   protected List<Document> getElementDocuments()
   {
      return getElement().getDocuments();
   }

   @Override
   protected List<Image> getElementImages()
   {
      return getElement().getImages();
   }

   @Override
   public String editDocsPage()
   {
      return EDIT_DOCS;
   }

   // ------------------------------------------------------

   private List<Product> products;

   public List<Product> getProducts()
   {
      if (products == null)
      {
         products = new ArrayList<Product>();
      }
      return products;
   }

   public String modProductsCurrent()
   {
      // TODO Auto-generated method stub
      super.modCurrent();
      loadProducts();
      return EDIT_PRODS + REDIRECT_PARAM;
   }

   private void loadProducts()
   {
      this.products = null;
      Search<CustomerToProduct> s = new Search<CustomerToProduct>(CustomerToProduct.class);
      s.getObj().setCustomer(getElement());
      for (CustomerToProduct ctp : customerToProductRepository.getList(s, 0, 0))
      {
         getProducts().add(ctp.getProduct());
      }
   }

   public String modProducts()
   {
      super.modElement();
      loadProducts();
      return EDIT_PRODS + REDIRECT_PARAM;
   }

   public void chooseProduct()
   {
      productController.reload();
   }

   public void pickProduct()
   {
      productController.modElement();
      Product product = productController.getElement();
      for (Product p : getProducts())
      {
         if (p.getId().equals(product.getId()))
         {
            logger.info(p.getId() + " already associated");
            return;
         }
      }
      CustomerToProduct ctp = new CustomerToProduct();
      ctp.setProduct(product);
      ctp.setCustomer(getElement());
      customerToProductRepository.persist(ctp);
      getProducts().add(product);
   }

   public void removeProduct(Long order)
   {
      removeProduct(order.intValue());
   }

   public void removeProduct(Integer order)
   {
      if (order != null && order >= 0 && getElement() != null
               && getProducts() != null
               && getProducts().size() > 0 && getProducts().size() > order)
      {
         Product toRemove = getProducts().get(order);
         getProducts().remove(toRemove);
         if (toRemove.getId() != null)
         {
            Search<CustomerToProduct> s = new Search<CustomerToProduct>(CustomerToProduct.class);
            s.getObj().setCustomer(getElement());
            for (CustomerToProduct ctp : customerToProductRepository.getList(s, 0, 0))
            {
               if (ctp.getProduct().getId().equals(toRemove.getId()))
               {
                  customerToProductRepository.delete(ctp.getId());
                  break;
               }
            }
         }
      }
      else
      {
         logger.info("removeProduct: non posso rimuovere posizione :" + order);
      }
   }

}
