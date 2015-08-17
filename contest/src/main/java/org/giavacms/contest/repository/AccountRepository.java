package org.giavacms.contest.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.contest.model.Account;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Named
@Stateless
@LocalBean
public class AccountRepository extends BaseRepository<Account>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "uid desc";
   }

   @Override
   protected void applyRestrictions(Search<Account> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      //CREATED IN DATE
      if (search.getObj() != null && search.getObj().getCreated() != null)
      {
         sb.append(separator).append(alias).append(".created = :CREATED ");
         params.put("CREATED", search.getObj().getCreated());
         separator = " and ";
      }

      // PHONE
      if (search.getObj() != null && search.getObj().getPhone() != null)
      {
         sb.append(separator).append(alias).append(".phone = :PHONE ");
         params.put("PHONE", search.getObj().getPhone());
         separator = " and ";
      }

      // NAME
      if (search.getObj() != null && search.getObj().getName() != null)
      {
         sb.append(separator).append(alias).append(".name = :NAME ");
         params.put("NAME", search.getObj().getName());
         separator = " and ";
      }

      // SURNAME
      if (search.getObj() != null && search.getObj().getSurname() != null)
      {
         sb.append(separator).append(alias).append(".surname = :SURNAME ");
         params.put("SURNAME", search.getObj().getSurname());
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public Account exist(String phone)
   {
      Account acount = null;
      try
      {
         acount = (Account) getEm()
                  .createQuery("select a from " + Account.class.getName()
                           + " a WHERE a.phone = :PHONE1 or a.phone = :PHONE2")
                  .setParameter("PHONE1", phone)
                  .setParameter("PHONE2", "39" + phone)
                  .getSingleResult();
      }
      catch (Exception e)
      {
      }
      return acount;
   }

   public void deleteNotConfirmed()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MINUTE, -5);
      int result = getEm().createNativeQuery("DELETE FROM " + Account.TABLE_NAME
               + " WHERE confirmed IS NULL "
               + " AND created < :CREATED ")
               .setParameter("CREATED", calendar.getTime()).executeUpdate();
      logger.info("DELETED NOT CONFIRMED ACCOUNT - " + " num. " + result);
   }

   public int confirmAccount(String phone)
   {
      int result = 0;
      if (phone.startsWith("39"))
      {
         result = getEm().createNativeQuery("UPDATE " + Account.TABLE_NAME
                  + " SET confirmed=:CONFIRMED "
                  + " WHERE phone = :PHONE "
                  + " AND created IS NOT NULL ")
                  .setParameter("PHONE", phone.substring(2))
                  .setParameter("CONFIRMED", new Date())
                  .executeUpdate();
         logger.info("CONFIRM ACCOUNT FOR PHONE - " + phone + ": num. " + result);
         if (result > 0)
         {
            return 1;
         }
      }
      result = getEm().createNativeQuery("UPDATE " + Account.TABLE_NAME
               + " SET confirmed=:CONFIRMED "
               + " WHERE phone = :PHONE "
               + " AND created IS NOT NULL ")
               .setParameter("PHONE", phone)
               .setParameter("CONFIRMED", new Date())
               .executeUpdate();
      logger.info("CONFIRM ACCOUNT FOR PHONE - " + phone + ": num. " + result);
      return result > 0 ? 2 : 0;

   }

   @Override protected Account prePersist(Account account) throws Exception
   {
      String uuid = UUID.randomUUID().toString();
      account.setUuid(uuid);
      return super.prePersist(account);
   }
}
