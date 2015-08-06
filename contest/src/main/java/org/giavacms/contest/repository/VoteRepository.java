package org.giavacms.contest.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.model.pojo.Ranking;
import org.giavacms.contest.model.pojo.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.*;

@Named
@Stateless
@LocalBean
public class VoteRepository extends BaseRepository<Vote>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "uuid desc";
   }

   @Override
   protected void applyRestrictions(Search<Vote> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // ONLY ACTIVES?
      if (search.getNot() != null && !search.getNot().isActive())
      {
         sb.append(separator).append(alias).append(".active = :ACTIVE ");
         params.put("ACTIVE", true);
         separator = " and ";
      }

      // ONLY CONFIRMED?
      if (search.getNot() != null && search.getNot().getConfirmed() != null)
      {
         sb.append(separator).append(alias).append(".confirmed IS NOT NULL ");
         separator = " and ";
      }

      //CREATED IN DATE
      if (search.getObj() != null && search.getObj().getCreated() != null)
      {
         sb.append(separator).append(alias).append(".created = :CREATED ");
         params.put("CREATED", search.getObj().getCreated());
         separator = " and ";
      }

      //CONFIRMED IN DATE
      if (search.getObj() != null && search.getObj().getConfirmed() != null)
      {
         sb.append(separator).append(alias).append(".confirmed = :CONFIRMED ");
         params.put("CONFIRMED", search.getObj().getConfirmed());
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

   public int confirmVote(String phone)
   {
      int result = 0;
      if (phone.startsWith("39"))
      {
         result = getEm().createNativeQuery("UPDATE " + Vote.TABLE_NAME
                  + " SET confirmed=:CONFIRMED "
                  + " WHERE phone = :PHONE "
                  + " AND active = :ACTIVE_W "
                  + " LIMIT 1 ")
                  .setParameter("PHONE", phone.substring(2))
                  .setParameter("ACTIVE_W", true)
                  .setParameter("CONFIRMED", new Date())
                  .executeUpdate();
         logger.info("CONFIRM VOTE FOR PHONE - " + phone + ": num. " + result);
         if (result > 0)
         {
            return 1;
         }
      }
      result = getEm().createNativeQuery("UPDATE " + Vote.TABLE_NAME
               + " SET confirmed=:CONFIRMED "
               + " WHERE phone = :PHONE "
               + " AND active = :ACTIVE_W "
               + " LIMIT 1 ")
               .setParameter("PHONE", phone)
               .setParameter("ACTIVE_W", true)
               .setParameter("CONFIRMED", new Date())
               .executeUpdate();
      logger.info("CONFIRM VOTE FOR PHONE - " + phone + ": num. " + result);
      return result > 0 ? 2 : 0;

   }

   public boolean isConfirmed(String phone)
   {
      phone = phone.replace(" ", "").replace(".", "").replace("+", "").replace("/", "")
               .replace("\\", "");
      java.math.BigInteger result = (java.math.BigInteger) getEm()
               .createNativeQuery("SELECT count(uuid) FROM " + Vote.TABLE_NAME
                        + " WHERE confirmed IS NOT NULL "
                        + " AND phone = :PHONE "
                        + " AND active = :ACTIVE_W ")
               .setParameter("PHONE", phone)
               .setParameter("ACTIVE_W", true)
               .getSingleResult();
      return (result != null && result.longValue() > 0) ? true : false;
   }

   public void passivateOldVotes()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MINUTE, -5);
      int result = getEm().createNativeQuery("UPDATE " + Vote.TABLE_NAME
               + " SET active = :ACTIVE "
               + " WHERE confirmed IS NULL "
               + " AND dateTime < :CREATED "
               + " AND active = :ACTIVE_W ")
               .setParameter("ACTIVE", false)
               .setParameter("CREATED", calendar.getTime())
               .setParameter("ACTIVE_W", true).executeUpdate();
      logger.info("PASSIVATE OLD VOTES - " + " num. " + result);
   }

   @Override
   protected Vote prePersist(Vote vote) throws Exception
   {

      vote.setActive(true);
      vote.setCreated(new Date());

      return vote;
   }

   public List<Ranking> getRanking(String preference, Date dateTime)
   {
      @SuppressWarnings("unchecked")
      List<Object[]> results = getEm().createNativeQuery(
               " SELECT count(*) as num, " + preference + " FROM " + Vote.TABLE_NAME
                        + " where confirmed IS NOT NULL "
                        + " and active = 1 "
                        + "and confirmed <= :dateTime"
                        + "  group by " + preference + " order by num desc")
               .setParameter("dateTime", dateTime)
               .getResultList();
      List<Ranking> rankings = new ArrayList<Ranking>();
      for (Object[] row : results)
      {
         Ranking ranking = new Ranking();
         ranking.setVotes(Integer.parseInt(row[0].toString()));
         ranking.setParticipationId((String) row[1]);
         ranking.setPreference(preference);
         rankings.add(ranking);
      }
      return rankings;
   }

   public Map<String, List<User>> getUsersForPreference() throws Exception
   {
      Map<String, List<User>> mapPref = new HashMap<>();
      Search<Vote> search = new Search<Vote>(Vote.class);
      search.getNot().setActive(true);
      search.getNot().setConfirmed(new Date());
      List<Vote> votes = getList(search, 0, 0);
      for (Vote vote : votes)
      {
         User user = new User(vote.getName(), vote.getPhone(), vote.getPreference1(), vote.getSurname(),
                  vote.getUuid());
         List<User> users;
         if (mapPref.containsKey(vote.getPreference1()))
         {
            users = mapPref.get(vote.getPreference1());
         }
         else
         {
            users = new ArrayList<>();
            mapPref.put(vote.getPreference1(), users);
         }
         users.add(user);
      }
      return mapPref;
   }

   public User getUserByPhone(String phone) throws Exception
   {
      List<Vote> votes = (List<Vote>) getEm()
               .createQuery(" select v from " + Vote.class.getName()
                        + " v where v.confirmed IS NOT NULL "
                        + " AND v.active = :ACTIVE_W "
                        + " AND v.phone = :PHONE "
                        + " AND v.name IS NOT NULL "
                        + " AND v.surname IS NOT NULL "
                        + " ORDER BY v.confirmed ASC")
               .setParameter("ACTIVE_W", true)
               .setParameter("PHONE", phone)
               .setMaxResults(1)
               .getResultList();
      for (Vote vote : votes)
      {
         User user = new User(vote.getName(), vote.getSurname());
         return user;
      }
      return null;
   }

   public List<User> getWinner(int size, String licenseNumber, List<String> alreadyWinners)
            throws Exception
   {
      List<User> winners = new ArrayList<>();
      List<Vote> votes = (List<Vote>) getEm()
               .createQuery(" select v from " + Vote.class.getName()
                        + " where preference1 != :LICENSE_NUMBER "
                        + " AND confirmed IS NOT NULL "
                        + " AND active = :ACTIVE_W "
                        + " AND phone NOT IN (:ALREADY_WINNERS)"
                        + " ORDER BY RANDOM()")
               .setParameter("LICENSE_NUMBER", licenseNumber)
               .setParameter("ACTIVE_W", true)
               .setParameter("ALREADY_WINNERS", alreadyWinners)
               .setMaxResults(size)
               .getResultList();
      for (Vote vote : votes)
      {
         User user = new User(vote.getName(), vote.getPhone(), vote.getPreference1(), vote.getSurname(),
                  vote.getUuid());
         winners.add(user);
      }
      return winners;
   }

   // SELECT photo, count(*) as num FROM Vote
   // where active=1 and confirmed != ''
   // group by photo
   // order by num desc
   // GO
   //
   // SELECT picture, count(*) as num FROM Vote
   // where active=1 and confirmed != ''
   // group by picture
   // order by num desc
   // GO
   //
   // SELECT sculpture, count(*) as num FROM Vote
   // where active=1 and confirmed != ''
   // group by sculpture
   // order by num desc
   // GO
}
