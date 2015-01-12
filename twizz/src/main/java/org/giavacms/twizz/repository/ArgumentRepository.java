package org.giavacms.twizz.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.twizz.model.Argument;

@Named
@Stateless
@LocalBean
public class ArgumentRepository extends AbstractRepository<Argument>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

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
      return "date asc";
   }

   @Override
   protected void applyRestrictions(Search<Argument> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

   }

   @Override
   public Argument fetch(Object key)
   {
      List<Argument> arguments = (List<Argument>) getEm()
               .createQuery(
                        "select A FROM " + Argument.class.getName()
                                 + " A LEFT JOIN FETCH A.questions Q WHERE A.id = :ID")
               .setParameter("ID", key).getResultList();
      if (arguments != null && arguments.size() > 0)
         return arguments.get(0);
      return null;
   }

   public Map<String, List<String>> getArgumentsByLanguages()
   {
      Map<String, List<String>> arguments = new HashMap<String, List<String>>();
      List<Argument> all = getAllList();
      for (Argument argument : all)
      {
         List<String> args;
         if (arguments.containsKey(argument.getLanguage()))
         {
            args = arguments.get(argument);
         }
         else
         {
            args = new ArrayList<String>();
            arguments.put(argument.getLanguage(), args);
         }
         args.add(argument.getName());
      }
      return arguments;
   }

}
