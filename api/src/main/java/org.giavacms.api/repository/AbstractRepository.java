package org.giavacms.api.repository;

import org.giavacms.api.model.Group;
import org.giavacms.api.model.Search;
import org.giavacms.api.util.RepositoryUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @param <T>
 * @author fiorenzo pizza
 */
public abstract class AbstractRepository<T> implements Serializable,
         Repository<T>
{

   private static final long serialVersionUID = 1L;

   // --- JPA ---------------------------------

   /**
    * @return
    */
   protected abstract EntityManager getEm();

   public abstract void setEm(EntityManager em);

   // --- Logger -------------------------------

   protected final Logger logger = Logger.getLogger(getClass().getName());

   // --- Mandatory logic --------------------------------

   // protected abstract Class<T> getEntityType();
   @SuppressWarnings("unchecked")
   protected Class<T> getEntityType() throws Exception
   {
      ParameterizedType parameterizedType = (ParameterizedType) getClass()
               .getGenericSuperclass();
      return (Class<T>) parameterizedType.getActualTypeArguments()[0];
   }

   // --- CRUD --------------

   public T create(Class<T> domainClass) throws Exception
   {
      return domainClass.newInstance();
   }

   public T persist(T object) throws Exception
   {
      object = prePersist(object);
      if (object != null)
      {
         getEm().persist(object);
      }
      return object;
   }

   protected T prePersist(T object) throws Exception
   {
      return object;
   }

   public T find(Object key) throws Exception
   {
      return getEm().find(getEntityType(), key);
   }

   public T fetch(Object key) throws Exception
   {
      return getEm().find(getEntityType(), key);
   }

   public T update(T object) throws Exception
   {
      object = preUpdate(object);
      getEm().merge(object);
      return object;
   }

   protected T preUpdate(T object) throws Exception
   {
      return object;
   }

   public void delete(Object key) throws Exception
   {
      T obj = getEm().find(getEntityType(), key);
      getEm().remove(obj);
   }

   // --- LIST ------------------------------------------

   @SuppressWarnings("unchecked")
   public List<T> getList(Search<T> search, int startRow, int pageSize)
            throws Exception
   {
      List<T> result = null;
      boolean count = false;
      Query res = getRestrictions(search, count);
      if (res == null)
         return result;
      if (startRow >= 0)
      {
         res.setFirstResult(startRow);
      }
      if (pageSize > 0)
      {
         res.setMaxResults(pageSize);
      }

      return (List<T>) res.getResultList();
   }

   public int getListSize(Search<T> search) throws Exception
   {
      boolean count = true;
      Query res = getRestrictions(search, count);
      return ((java.lang.Long) res.getSingleResult()).intValue();
   }

   /**
    * @param startRow
    * @param pageSize
    * @param res
    * @return
    */
   @SuppressWarnings("unchecked")
   public List<T> getList(int startRow, int pageSize, Query res)
            throws Exception
   {
      try
      {
         List<T> result = new ArrayList<T>();
         result = (List<T>) res.getResultList();
         if (result != null)
            return result;
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
      }
      return new ArrayList<T>();
   }

   /**
    * criteri di default, comuni a tutti, ma specializzabili da ogni EJB tramite overriding
    */
   protected Query getRestrictions(Search<T> search, boolean justCount)
            throws Exception
   {

      Map<String, Object> params = new HashMap<String, Object>();
      String alias = "c";
      StringBuffer sb = new StringBuffer(getBaseList(search.getObj()
               .getClass(), alias, justCount));
      String separator = " where ";

      applyRestrictions(search, alias, separator, sb, params);

      if (!justCount)
      {
         sb.append(getOrderBy(alias, search.getOrder()));
      }

      Query q = getEm().createQuery(sb.toString());
      for (String param : params.keySet())
      {
         q.setParameter(param, params.get(param));
      }

      return q;

   }

   /**
    * metodo da sovrascrivere per applicare parametri alla query, con relative condizioni d'uso
    * <p/>
    * esempio:
    * <p/>
    * String leftOuterJoinAlias = "s"; if (search.getObj().getNumero() != null &&
    * search.getObj().getNumero().trim().length() > 0) { sb.append(" left outer join ").append(alias)
    * .append(".serviziPrenotati ").append(leftOuterJoinAlias); // sb.append(" on "
    * ).append(leftOuterJoinAlias).append(".allegati.id = ").append (alias).append(".id"); }
    * <p/>
    * if (search.getObj().getAttivo() != null) { sb.append(separator).append(" ").append(alias)
    * .append(".attivo = :attivo "); // aggiunta alla mappa params.put("attivo", search.getObj().getAttivo()); //
    * separatore separator = " and "; }
    * <p/>
    * if (search.getObj().getNumero() != null && !search.getObj().getNumero().trim().isEmpty()) {
    * sb.append(separator).append(leftOuterJoinAlias) .append(".servizio.numero = :numero and ")
    * .append(leftOuterJoinAlias) .append(".servizio.tipo = :tipoServizio "); // aggiunta alla mappa
    * params.put("numero", search.getObj().getNumero()); params.put("tipoServizio", TipoServizioEnum.OMB); // separatore
    * separator = " and "; }
    *
    * @param search
    * @param alias
    * @param separator
    * @param sb
    * @param params
    */
   protected void applyRestrictions(Search<T> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
            throws Exception
   {
      String activeFieldName = RepositoryUtils.getActiveFieldName(getEntityType());
      if (activeFieldName != null)
      {
         sb.append(separator).append(alias).append(".").append(activeFieldName).append(" = :activeFieldValue ");
         params.put("activeFieldValue", true);
         separator = " and ";
      }
   }

   protected String getBaseList(Class<? extends Object> clazz, String alias,
            boolean count) throws Exception
   {
      if (count)
      {
         return "select count(" + alias + ") from " + clazz.getSimpleName()
                  + " " + alias + " ";
      }
      else
      {
         return "select " + alias + " from " + clazz.getSimpleName() + " "
                  + alias + " ";
      }
   }

   protected abstract String getDefaultOrderBy();

   public String getOrderBy(String alias, String orderBy) throws Exception
   {
      try
      {
         if (orderBy == null || orderBy.length() == 0)
         {
            orderBy = getDefaultOrderBy();
         }
         StringBuffer result = new StringBuffer();
         String[] orders = orderBy.split(",");
         for (String order : orders)
         {
            result.append(", ").append(alias).append(".")
                     .append(order.trim()).append(" ");
         }
         return " order by " + result.toString().substring(2);
      }
      catch (Exception e)
      {
         return "";
      }
   }

   protected String likeParam(String param)
   {
      return "%" + param + "%";
   }

   protected String likeParamL(String param)
   {
      return "%" + param;
   }

   protected String likeParamR(String param)
   {
      return param + "%";
   }

   @Override
   public boolean exist(Object key) throws Exception
   {
      String alias = "c";
      String idFieldName = RepositoryUtils.getIdFieldName(getEntityType());
      boolean justCount = true;
      StringBuffer sb = new StringBuffer(getBaseList(getEntityType(), alias,
               justCount));
      sb.append(" where " + alias + "." + idFieldName + " = :ID ");

      String activeFieldName = RepositoryUtils.getActiveFieldName(getEntityType());
      if (activeFieldName != null)
      {
         sb.append(" and " + alias + "." + activeFieldName + " = :ACTIVE ");
      }
      Query q = getEm().createQuery(sb.toString()).setParameter("ID", key);
      if (activeFieldName != null)
      {
         q.setParameter("ACTIVE", true);
      }
      return ((Long) q.getSingleResult()).intValue() > 0;
   }

   /**
    * Override this is needed
    */
   public Object castId(String key) throws Exception
   {
      return RepositoryUtils.castId(key, getEntityType());
   }

   public String getUniqueKey(String key) throws Exception
   {
      String keyNotUsed = key;
      boolean found = false;
      int i = 0;
      while (!found)
      {
         logger.info("key to search: " + keyNotUsed);
         boolean exist = exist(keyNotUsed);
         logger.info("found " + exist + " pages with id: " + keyNotUsed);
         if (exist)
         {
            i++;
            keyNotUsed = key + "-" + i;
         }
         else
         {
            found = true;
            return keyNotUsed;
         }
      }
      return "";
   }

   @SuppressWarnings("unchecked")
   public List<Group<T>> getGroups(Search<T> search, int startRow, int pageSize)
   {
      List<Group<T>> result = new ArrayList<Group<T>>();
      try
      {
         if (search.getGrouping() == null || search.getGrouping().trim().length() == 0
                  || search.getGrouping().trim().split(",").length == 0)
         {
            List<T> list = getList(search, startRow, pageSize);
            for (T t : list)
            {
               result.add(new Group<T>(1L, t));
            }
            return result;
         }
         Map<String, Object> params = new HashMap<String, Object>();
         String alias = "c";
         StringBuffer sb = new StringBuffer();
         String groups[] = search.getGrouping().trim().split(",");
         String countAlias = "counting";
         sb.append("select count(").append(alias).append(".").append(groups[0]).append(") as ").append(countAlias)
                  .append(", ");
         for (int i = 0; i < groups.length; i++)
         {
            sb.append(alias).append(".").append(groups[i]).append(i == groups.length - 1 ? "" : ", ");
         }
         sb.append(" from ").append(search.getObj().getClass().getSimpleName()).append(" ").append(alias);
         String separator = " where ";
         applyRestrictions(search, alias, separator, sb, params);
         sb.append(" group by ");
         for (int i = 0; i < groups.length; i++)
         {
            sb.append(alias).append(".").append(groups[i]).append(i == groups.length - 1 ? "" : ", ");
         }
         sb.append(" order by ").append(countAlias).append(" desc ");
         Query q = getEm().createQuery(sb.toString());
         for (String param : params.keySet())
         {
            q.setParameter(param, params.get(param));
         }
         if (startRow >= 0)
         {
            q.setFirstResult(startRow);
         }
         if (pageSize > 0)
         {
            q.setMaxResults(pageSize);
         }
         List<Object[]> resultList = (List<Object[]>) q.getResultList();
         if (resultList == null || resultList.size() == 0)
         {
            return result;
         }
         Long max = (Long) resultList.get(0)[0];
         for (Object[] resultItem : resultList)
         {
            T t = construct(Arrays.asList(groups), Arrays.asList(resultItem)
                     .subList(1, resultItem.length));
            if (t != null)
            {
               result.add(new Group<T>((Long) resultItem[0], t, max));
            }
         }
         return result;
      }
      catch (Exception ex)
      {
         logger.error(ex.getMessage(), ex);
         return result;
      }
   }

   /**
    * Override this
    *
    * @param asList
    * @param subList
    * @return
    */
   protected T construct(List<String> fieldNames, List<Object> fieldValues)
   {
      throw new RuntimeException("not implemented. override this.");
   }

}
