package org.giavacms.contest.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.contest.model.Token;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class TokenRepository extends BaseRepository<Token>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "uuid desc";
   }

   @Override
   protected void applyRestrictions(Search<Token> search, String alias, String separator, StringBuffer sb,
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
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public Token exist(String phone)
   {
      Token token = null;
      try
      {
         token = (Token) getEm()
                  .createQuery("select a from " + Token.class.getName()
                           + " a WHERE a.phone = :PHONE AND a.created IS NOT NULL "
                           + " AND a.confirmed IS NOT NULL  AND a.destroyed IS NULL")

                  .setParameter("PHONE", phone).getSingleResult();
      }
      catch (Exception e)
      {
      }
      return token;
   }

   public int confirmToken(String phone)
   {
      int result = 0;
      if (phone.startsWith("39"))
      {
         result = getEm().createNativeQuery("UPDATE " + Token.TABLE_NAME
                  + " SET confirmed=:CONFIRMED "
                  + " WHERE phone = :PHONE "
                  + " AND created IS NOT NULL ")
                  .setParameter("PHONE", phone.substring(2))
                  .setParameter("CONFIRMED", new Date())
                  .executeUpdate();
         logger.info("CONFIRM TOKEN FOR PHONE - " + phone + ": num. " + result);
         if (result > 0)
         {
            return 1;
         }
      }
      result = getEm().createNativeQuery("UPDATE " + Token.TABLE_NAME
               + " SET confirmed=:CONFIRMED "
               + " WHERE phone = :PHONE "
               + " AND created IS NOT NULL ")
               .setParameter("PHONE", phone)
               .setParameter("CONFIRMED", new Date())
               .executeUpdate();
      logger.info("CONFIRM TOKEN FOR PHONE - " + phone + ": num. " + result);
      return result > 0 ? 2 : 0;
   }

   public void deleteNotConfirmed()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MINUTE, -5);
      int result = getEm().createNativeQuery("DELETE FROM " + Token.TABLE_NAME
               + " WHERE confirmed IS NULL "
               + " AND created < :CREATED ")
               .setParameter("CREATED", calendar.getTime()).executeUpdate();
      logger.info("DELETED NOT CONFIRMED TOKEN - " + " num. " + result);
   }
}
