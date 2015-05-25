/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.paypalweb.model.IpnContent;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.List;

@Named
@Stateless
public class IpnContentRepository extends
         BaseRepository<IpnContent>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "id desc";
   }

   public IpnContent findByTxnId(String txnId, Long id)
   {
      try
      {
         List<IpnContent> list = getEm()
                  .createQuery(
                           "select t from IpnContent t where t.txnId = :TXNID and t.id != :CURRENT_ID")
                  .setParameter("TXNID", txnId).setParameter("CURRENT_ID", id).getResultList();
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

   public IpnContent findByCustom(String custom)
   {
      try
      {
         List<IpnContent> list = getEm()
                  .createQuery(
                           "select t from IpnContent t where t.custom = :CUSTOM ")
                  .setParameter("CUSTOM", custom).getResultList();
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

}
