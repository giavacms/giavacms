package org.giavacms.people.producer;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.producer.AbstractProducer;
import org.giavacms.people.model.PeopleType;
import org.giavacms.people.repository.PeopleTypeRepository;

@Named
@SessionScoped
public class PeopleProducer extends AbstractProducer
{

   private static final long serialVersionUID = 1L;

   @Inject
   PeopleTypeRepository peopleTypeRepository;

   public PeopleProducer()
   {
   }

   @Produces
   @Named
   public SelectItem[] getPeopleTypeItems()
   {
      if (items.get(PeopleType.class) == null)
      {
         List<PeopleType> peopleTypes = peopleTypeRepository.getAllList();
         SelectItem[] peopleTypeItems = new SelectItem[peopleTypes.size()];
         for (int i = 0; i < peopleTypeItems.length; i++)
         {
            peopleTypeItems[i] = new SelectItem(peopleTypes.get(i).getRichContentType().getId(), peopleTypes.get(i)
                     .getRichContentType().getName());
         }
         items.put(PeopleType.class, peopleTypeItems);
      }
      return items.get(PeopleType.class);
   }

}
