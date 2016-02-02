/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.repository;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.security.model.UserRole;
import org.jboss.logging.Logger;

@Named
@Stateless
public class UserRoleRepository extends BaseRepository<UserRole> implements
         Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @PersistenceContext
   EntityManager em;

   public UserRoleRepository()
   {
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "id asc";
   }

   @Override
   protected void applyRestrictions(Search<UserRole> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      if (search.getObj().getUserAuth() != null && search.getObj().getUserAuth().getId() != null)
      {
         sb.append(separator).append(alias).append(".userAuth.id = :userAuthId ");
         params.put("userAuthId", search.getObj().getUserAuth().getId());
         separator = " and ";
      }
      super.applyRestrictions(search, alias, separator, sb, params);
   }
}
