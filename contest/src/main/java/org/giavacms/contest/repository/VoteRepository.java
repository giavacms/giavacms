package org.giavacms.contest.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.contest.model.Vote;
import org.giavacms.contest.model.pojo.Ranking;

@Named
@Stateless
@LocalBean
public class VoteRepository extends BaseRepository<Vote>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "uid desc";
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

   public void confirmVote(String phone)
   {
      int result = getEm().createNativeQuery("UPDATE " + Vote.TABLE_NAME
               + " SET confirmed=:CONFIRMED "
               + " WHERE phone = :PHONE "
               + " AND active = :ACTIVE_W ")
               .setParameter("PHONE", phone)
               .setParameter("ACTIVE_W", true)
               .setParameter("CONFIRMED", new Date())
               .executeUpdate();
      logger.info("CONFIRM VOTE FOR PHONE - " + phone + ": num. " + result);
   }

   public void passivateOldVotes()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MINUTE, -5);
      int result = getEm().createNativeQuery("UPDATE " + Vote.TABLE_NAME
               + " SET active = :ACTIVE "
               + " WHERE confirmed = :CONFIRMED "
               + " AND created < :CREATED "
               + " AND active = :ACTIVE_W ")
               .setParameter("ACTIVE", false)
               .setParameter("CONFIRMED", null)
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

   public List<Ranking> getRanking(String discipline)
   {
      @SuppressWarnings("unchecked")
      List<Object[]> results = getEm().createNativeQuery(
               " SELECT count(*) as num, " + discipline + " FROM " + Vote.TABLE_NAME
                        + " where active = 1 and confirmed != '' group by " + discipline + " order by num desc")
               .getResultList();
      List<Ranking> rankings = new ArrayList<Ranking>();
      for (Object[] row : results)
      {
         Ranking ranking = new Ranking();
         ranking.setVotes(Integer.parseInt(row[0].toString()));
         ranking.setParticipationId((String) row[1]);
         ranking.setDiscipline(discipline);
         rankings.add(ranking);
      }
      return rankings;
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
