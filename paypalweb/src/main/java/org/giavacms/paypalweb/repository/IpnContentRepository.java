package org.giavacms.paypalweb.repository;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.paypalweb.model.IpnContent;

@Named
@Stateless
@LocalBean
public class IpnContentRepository extends
         AbstractRepository<IpnContent>
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
      return "id desc";
   }

   public IpnContent findByTxnId(String txnId)
   {
      try
      {
         List<IpnContent> list = em
                  .createQuery(
                           "select t from IpnContent t where t.txnId = :TXNID")
                  .setParameter("TXNID", txnId).getResultList();
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
