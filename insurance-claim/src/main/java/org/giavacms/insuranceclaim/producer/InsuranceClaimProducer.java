package org.giavacms.insuranceclaim.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.insuranceclaim.model.InsuranceClaimCategory;
import org.giavacms.insuranceclaim.model.InsuranceClaimTypology;
import org.giavacms.insuranceclaim.repository.InsuranceClaimCategoryRepository;
import org.giavacms.insuranceclaim.repository.InsuranceClaimTypologyRepository;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class InsuranceClaimProducer implements Serializable
{

   Logger logger = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;

   @Inject
   private InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

   @Inject
   private InsuranceClaimTypologyRepository insuranceClaimTypologyRepository;

   @SuppressWarnings("rawtypes")
   private Map<Class, SelectItem[]> items = null;

   public InsuranceClaimProducer()
   {
      // TODO Auto-generated constructor stub
   }

   @Produces
   @Named
   public SelectItem[] getInsuranceClaimCategoryItems()
   {
      if (items.get(InsuranceClaimCategory.class) == null)
      {
         List<SelectItem> valori = new ArrayList<SelectItem>();
         valori.add(new SelectItem(null, "seleziona categoria..."));
         for (InsuranceClaimCategory t : insuranceClaimCategoryRepository
                  .getList(new Search<InsuranceClaimCategory>(
                           InsuranceClaimCategory.class), 0, 0))
         {
            valori.add(new SelectItem(t.getId(), t
                     .getInsuranceClaimTypology().getName()
                     + " - "
                     + t.getName()));
         }
         items.put(InsuranceClaimCategory.class,
                  valori.toArray(new SelectItem[] {}));
      }
      return items.get(InsuranceClaimCategory.class);
   }

   @Produces
   @Named
   public SelectItem[] getInsuranceClaimTypologyItems()
   {
      if (items.get(InsuranceClaimTypology.class) == null)
      {
         items.put(InsuranceClaimTypology.class, JSFUtils.setupItems(
                  new Search<InsuranceClaimTypology>(
                           InsuranceClaimTypology.class),
                  insuranceClaimTypologyRepository, "id", "name",
                  "nessuna tipologia", "seleziona tipologia..."));
      }
      return items.get(InsuranceClaimTypology.class);
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
