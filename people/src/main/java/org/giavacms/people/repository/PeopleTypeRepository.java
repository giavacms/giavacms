package org.giavacms.people.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.people.model.PeopleType;

@Named
@Stateless
@LocalBean
public class PeopleTypeRepository extends AbstractRepository<PeopleType>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   public EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "richContentType.name asc";
   }

}
