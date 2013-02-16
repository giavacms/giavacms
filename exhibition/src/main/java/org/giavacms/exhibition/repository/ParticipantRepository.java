package org.giavacms.exhibition.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Discipline;
import org.giavacms.exhibition.model.Participant;
import org.giavacms.exhibition.model.pojo.ParticipantExhibition;

@Named
@Stateless
@LocalBean
public class ParticipantRepository extends AbstractRepository<Participant>
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
      // TODO Auto-generated method stub
      return "subject.surname asc, subject.name asc, id desc";
   }

   @Override
   protected void applyRestrictions(Search<Participant> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";
      // EXHIBITION
      if (search.getObj().getExhibition() != null
               && search.getObj().getExhibition().getId() != null
               && !search.getObj().getExhibition().getId().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".exhibition.id = :EXHIBITION ");
         params.put("EXHIBITION", search.getObj().getExhibition().getId());
      }
      // DISCIPLINE
      if (search.getObj().getDiscipline() != null
               && search.getObj().getDiscipline().getId() != null
               && !search.getObj().getDiscipline().getId().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".discipline.id = :DISCIPLINE ");
         params.put("DISCIPLINE", search.getObj().getDiscipline().getId());
      }
      // SUBJECT
      if (search.getObj().getSubject() != null
               && search.getObj().getSubject().getId() != null
               && !search.getObj().getSubject().getId().isEmpty())
      {
         sb.append(separator).append(alias).append(".subject.id = :SUBJECT ");
         params.put("SUBJECT", search.getObj().getSubject().getId());
      }

      // SUBJECT SURNAME
      if (search.getObj().getSubject() != null
               && search.getObj().getSubject().getSurname() != null
               && !search.getObj().getSubject().getSurname().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".subject.surname LIKE :SUBJECT_SURNAME ");
         params.put("SUBJECT_SURNAME", likeParam(search.getObj().getSubject()
                  .getSurname()));
      }

      // SUBJECT NAME
      if (search.getObj().getSubject() != null
               && search.getObj().getSubject().getName() != null
               && !search.getObj().getSubject().getName().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".subject.name LIKE :SUBJECT_NAME ");
         params.put("SUBJECT_NAME", likeParam(search.getObj().getSubject()
                  .getName()));
      }

      // TYPE
      if (search.getObj().getSubject() != null
               && search.getObj().getSubject().getType() != null
               && !search.getObj().getSubject().getType().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".subject.type = :SUBJECT_TYPE ");
         params.put("SUBJECT_TYPE", search.getObj().getSubject()
                  .getType());
      }
   }

   public List<ParticipantExhibition> getAllPartecipantsBySubject(String subjectId)
   {
      List<ParticipantExhibition> participantExhibitions = new ArrayList<ParticipantExhibition>();
      Iterator<Object[]> results = getEm()
               .createNativeQuery(
                        "SELECT P.id as partecipant_id, P.subject_id, S.type, E.id as exhibitionId, E.name " +
                                 " FROM `ExhibitionParticipant` P " +
                                 " LEFT JOIN Exhibition E  ON P.exhibition_id= E.id " +
                                 " LEFT JOIN ExhibitionSubject S ON P.subject_id = S.id" +
                                 " WHERE P.subject_id = :SUBJECT_ID")

               .setParameter("SUBJECT_ID", subjectId)
               .getResultList().iterator();
      while (results.hasNext())
      {
         Object[] row = results.next();
         int i = 0;
         BigInteger partecipant_id = null;
         if (row[i] instanceof BigInteger)
            partecipant_id = (BigInteger) row[i];
         i++;
         String subject_id = (String) row[i];
         i++;
         String type = (String) row[i];
         i++;
         String id = (String) row[i];
         i++;
         String name = (String) row[i];
         ParticipantExhibition l = new ParticipantExhibition();
         if (partecipant_id != null)
            l.setPartecipant_id(partecipant_id.longValue());
         l.setSubject_id(subject_id);
         l.setType(type);
         l.setId(id);
         l.setName(name);
         participantExhibitions.add(l);

      }
      return participantExhibitions;
   }

   @Override
   protected Participant prePersist(Participant participant)
   {
      if (participant.getDiscipline().getId() == null)
         participant.setDiscipline(null);
      return super.prePersist(participant);
   }

   @Override
   protected Participant preUpdate(Participant participant)
   {
      if (participant.getDiscipline().getId() == null)
         participant.setDiscipline(null);
      return super.preUpdate(participant);
   }

   @Override
   public boolean delete(Object key)
   {
      try
      {
         Participant participant = getEm().find(getEntityType(), key);
         if (participant != null)
         {
            participant.setActive(false);
            getEm().merge(participant);
         }
         return true;
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, null, e);
         return false;
      }
   }

   @Override
   public Participant fetch(Object key)
   {
      try
      {
         Long id;
         if (key instanceof String)
         {
            id = Long.valueOf((String) key);
         }
         else if (key instanceof Long)
         {
            id = (Long) key;
         }
         else
         {
            throw new Exception("key type is not correct!!");
         }
         Participant participant = find(id);
         return participant;
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, null, e);
         return null;
      }
   }

   public Participant findLatestPartcipantBySubjectId(String subjectId)
   {
      Search<Participant> s = new Search<Participant>(Participant.class);
      s.getObj().getSubject().setId(subjectId);
      List<Participant> list = getList(s, 0, 1);
      if (list != null && list.size() > 0)
         return list.get(0);
      return new Participant();
   }

   public List<Discipline> getDistinctDiscipline(String exhibition)
   {
      List<Discipline> disciplines = new ArrayList<Discipline>();
      Iterator<Object[]> results = getEm()
               .createNativeQuery(
                        "SELECT  d.id, d.name, COUNT(d.id) FROM ExhibitionParticipant AS p "
                                 + " LEFT JOIN ExhibitionDiscipline AS d ON ( d.id = p.discipline_id ) "
                                 + " WHERE  p.exhibition_id = :EXHIBITION "
                                 + "GROUP BY d.id")
               .setParameter("EXHIBITION", exhibition).getResultList()
               .iterator();
      while (results.hasNext())
      {
         Object[] row = results.next();
         Discipline discipline = new Discipline();
         discipline.setId((String) row[0]);
         discipline.setName((String) row[1]);
         if (row[2] instanceof BigInteger)
         {
            BigInteger num = (BigInteger) row[2];
            discipline.setNum(num.intValue());
         }
         else if (row[2] instanceof Integer)
         {
            Integer num = (Integer) row[2];
            discipline.setNum(num.intValue());
         }
         disciplines.add(discipline);
      }
      return disciplines;
   }
}
