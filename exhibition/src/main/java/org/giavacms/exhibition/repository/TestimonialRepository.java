package org.giavacms.exhibition.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Testimonial;

@Named
@Stateless
@LocalBean
public class TestimonialRepository extends AbstractRepository<Testimonial>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Inject
   SubjectRepository subjectRepository;

   @Override
   protected EntityManager getEm()
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
      // TODO Auto-generated method stub
      return "surname asc";
   }

   @Override
   protected Testimonial prePersist(Testimonial testimonial)
   {
      String idTitle = PageUtils.createPageId(testimonial.getNameSurname());
      String idFinal = subjectRepository.testKey(idTitle);
      testimonial.setId(idFinal);
      return testimonial;
   }

   @Override
   protected void applyRestrictions(Search<Testimonial> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";
      // NAME
      if (search.getObj().getName() != null
               && search.getObj().getName().trim().length() > 0)
      {
         sb.append(separator).append(alias).append(".name = :NAME ");
         params.put("NAME", search.getObj().getName());
      }
      // SURNAME
      if (search.getObj().getSurname() != null
               && !search.getObj().getSurname().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".surname LIKE :SURNAME ");
         params.put("SURNAME", likeParam(search.getObj().getSurname()));
      }
   }

   @Override
   public boolean delete(Object key)
   {
      try
      {
         Testimonial testimonial = getEm().find(getEntityType(), key);
         if (testimonial != null)
         {
            testimonial.setActive(false);
            getEm().merge(testimonial);
         }
         return true;
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, null, e);
         return false;
      }
   }

}
