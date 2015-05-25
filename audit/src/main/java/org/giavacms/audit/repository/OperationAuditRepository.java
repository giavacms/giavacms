/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.audit.repository;

import org.giavacms.api.model.Search;
import org.giavacms.audit.model.OperationAudit;
import org.giavacms.base.repository.BaseRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class OperationAuditRepository extends BaseRepository<OperationAudit>
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
   protected void applyRestrictions(Search<OperationAudit> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {

      if (search.getObj().getData() != null)
      {
         sb.append(separator).append(alias).append(".data = :data ");
         params.put("data", search.getObj().getData());
      }
      if (search.getObj().getUsername() != null
               && !search.getObj().getUsername().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".username LIKE :username ");
         params.put("username", likeParam(search.getObj().getUsername()));
      }
      if (search.getObj().getTipo() != null
               && !search.getObj().getTipo().isEmpty())
      {
         sb.append(separator).append(alias).append(".tipo = :type ");
         params.put("type", search.getObj().getTipo());
      }

   }

   @Override
   protected OperationAudit prePersist(OperationAudit n)
   {
      if (n.getData() == null)
         n.setData(new Date());
      return n;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "data desc";
   }
}
