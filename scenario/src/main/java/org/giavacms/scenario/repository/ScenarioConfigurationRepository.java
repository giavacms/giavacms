package org.giavacms.scenario.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.scenario.model.ScenarioConfiguration;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@Stateless
@LocalBean
public class ScenarioConfigurationRepository extends
         BaseRepository<ScenarioConfiguration>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected String getDefaultOrderBy()
   {
      // TODO Auto-generated method stub
      return "id asc";
   }

   public ScenarioConfiguration load() throws Exception
   {
      ScenarioConfiguration c = null;
      try
      {
         c = find(1L);
      }
      catch (Exception e)
      {
      }
      if (c == null)
      {
         c = new ScenarioConfiguration();
         c.setResize(false);
         c.setMaxWidthOrHeight(0);
         persist(c);
      }
      return c;
   }

}
