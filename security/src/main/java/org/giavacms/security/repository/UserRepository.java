/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.security.model.UserAuth;
import org.giavacms.security.model.UserRole;
import org.giavacms.security.util.PasswordUtils;
import org.jboss.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class UserRepository extends BaseRepository<UserAuth> implements
         Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @PersistenceContext
   EntityManager em;

   public UserRepository()
   {
   }

   @Override
   protected void applyRestrictions(Search<UserAuth> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      boolean byRole = search.getObj().getRole() != null
               && !search.getObj().getRole().isEmpty();

      String roleLeftJoinAlias = "r";
      if (byRole)
      {
         sb.append("left join ").append(alias).append(".userRoles ")
                  .append(roleLeftJoinAlias).append(" ");
      }

      // TODO Auto-generated method stub
      // NAME
      if (search.getObj().getName() != null
               && !search.getObj().getName().isEmpty())
      {
         sb.append(separator + " upper(").append(alias)
                  .append(".name) LIKE :NAME ");
         params.put("NAME", likeParam(search.getObj().getName()));
      }
      // USERNAME
      if (search.getObj().getUsername() != null
               && !search.getObj().getUsername().isEmpty())
      {
         sb.append(separator + " upper(").append(alias)
                  .append(".username) LIKE :USERNAME ");
         params.put("USERNAME", likeParam(search.getObj().getUsername()));
      }

      // ROLE
      if (byRole)
      {
         sb.append(separator).append(roleLeftJoinAlias)
                  .append(".roleName = :ROLE ");
         params.put("ROLE", search.getObj().getRole());
      }

   }

   public void verifyConfiguration()
   {
      List<UserAuth> list = em
               .createQuery(
                        "select t from UserAuth t left join fetch t.userRoles r where r.roleName = :ROLEADMIN")
               .setParameter("ROLEADMIN", "admin").getResultList();
      if (list == null || list.size() == 0)
      {
         UserAuth user = new UserAuth();
         user.setName("admin");
         user.setPassword(PasswordUtils.createPassword("admin"));
         user.setUsername("admin");
         user.setAdmin(true);
         persist(user);
         logger.info("admin system created");
      }
      else
      {
         logger.info("admin system exist");
      }
   }

   public UserAuth findByUsername(String username)
   {
      try
      {
         List<UserAuth> list = em
                  .createQuery(
                           "select t from UserAuth t where t.username = :USERNAME")
                  .setParameter("USERNAME", username).getResultList();
         if (list != null && list.size() > 0)
            return list.get(0);
         return null;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   public UserAuth persist(UserAuth userAuth)
   {
      try
      {
         if (userAuth.isAdmin())
         {
            UserRole role = new UserRole();
            role.setRoleName("admin");
            role.setUserAuth(userAuth);
            userAuth.addUserRole(role);
         }
         else if (userAuth.getRoles() != null
                  && userAuth.getRoles().size() > 0)
         {
            UserRole role = new UserRole();
            role.setRoleName("user");
            role.setUserAuth(userAuth);
            userAuth.addUserRole(role);
            for (String roleName : userAuth.getRoles())
            {
               role = new UserRole();
               role.setRoleName(roleName);
               role.setUserAuth(userAuth);
               userAuth.addUserRole(role);
            }
         }
         em.persist(userAuth);
         return userAuth;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override public UserAuth update(UserAuth userAuth) throws Exception
   {

      if (userAuth.isAdmin())
      {
         userAuth.setUserRoles(null);
         UserRole role = new UserRole();
         role.setRoleName("admin");
         role.setUserAuth(userAuth);
         userAuth.addUserRole(role);
         em.persist(role);
      }
      else if (userAuth.getRoles() != null
               && userAuth.getRoles().size() > 0)
      {
         userAuth.setUserRoles(null);
         em.merge(userAuth);
         UserRole role = new UserRole();
         role.setRoleName("user");
         role.setUserAuth(userAuth);
         userAuth.addUserRole(role);
         for (String roleName : userAuth.getRoles())
         {
            role = new UserRole();
            role.setUserAuth(userAuth);
            role.setRoleName(roleName);
            userAuth.addUserRole(role);
            em.persist(role);
         }
      }
      em.merge(userAuth);
      return userAuth;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "username asc";
   }

}
