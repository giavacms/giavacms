package org.giavacms.contest.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.contest.model.Account;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

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
                  .createQuery("select a from " + Account.class.getName() + " a WHERE a.phone = :PHONE ")
                  .setParameter("PHONE", phone).getSingleResult();
      }
      catch (Exception e)
      {
      }
      return acount;
   }
}
