package org.giavacms.customer.producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.customer.model.CustomerCategory;
import org.giavacms.customer.repository.CustomerCategoryRepository;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class CustomerProducer implements Serializable
{

   Logger logger = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;

   @Inject
   private CustomerCategoryRepository categoryRepository;

   @SuppressWarnings("rawtypes")
   private Map<Class, SelectItem[]> items = null;

   public CustomerProducer()
   {
      // TODO Auto-generated constructor stub
   }

   @Produces
   @Named
   public SelectItem[] getCustomerCategoryItems()
   {
      if (items.get(CustomerCategory.class) == null)
      {
         items.put(CustomerCategory.class, JSFUtils.setupItems(new Search<CustomerCategory>(
                  CustomerCategory.class), categoryRepository, "id", "name",
                  "nessuna categoria", "seleziona categoria..."));
      }
      return items.get(CustomerCategory.class);
   }

   @SuppressWarnings("rawtypes")
   public void resetItemsForClass(Class clazz)
   {
      if (items.containsKey(clazz))
      {
         items.remove(clazz);
      }
   }

   // ==============================================================================

   @SuppressWarnings("rawtypes")
   @PostConstruct
   public void reset()
   {
      items = new HashMap<Class, SelectItem[]>();
   }

}
