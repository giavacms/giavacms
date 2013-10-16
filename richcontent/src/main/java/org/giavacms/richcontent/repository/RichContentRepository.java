package org.giavacms.richcontent.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.controller.util.TimeUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.StringUtils;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.model.type.RichContentType;

@Named
@Stateless
@LocalBean
public class RichContentRepository extends AbstractPageRepository<RichContent>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected RichContent prePersist(RichContent n)
   {
      n.setClone(true);
      if (n.getDate() == null)
         n.setDate(new Date());
      if (n.getRichContentType() != null
               && n.getRichContentType().getId() != null)
         n.setRichContentType(getEm().find(RichContentType.class,
                  n.getRichContentType().getId()));
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
      n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
      return super.prePersist(n);
   }

   @Override
   protected RichContent preUpdate(RichContent n)
   {
      n.setClone(true);
      if (n.getDate() == null)
         n.setDate(new Date());
      if (n.getRichContentType() != null
               && n.getRichContentType().getId() != null)
         n.setRichContentType(getEm().find(RichContentType.class,
                  n.getRichContentType().getId()));
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
      n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
      n = super.preUpdate(n);
      return n;
   }

   public RichContent findLast()
   {
      RichContent ret = new RichContent();
      try
      {
         ret = (RichContent) getEm()
                  .createQuery(
                           "select p from "
                                    + RichContent.class.getSimpleName()
                                    + " p order by p.date desc ")
                  .setMaxResults(1).getSingleResult();
         if (ret == null)
         {
            return null;
         }
         else
         {
            return this.fetch(ret.getId());
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return ret;
   }

   public RichContent getLast(String category)
   {
      Search<RichContent> r = new Search<RichContent>(RichContent.class);
      r.getObj().getRichContentType().setName(category);
      List<RichContent> list = getList(r, 0, 1);
      return list.size() > 0 ? list.get(0) : new RichContent();
   }

   public RichContent findHighlight()
   {
      try
      {
         String retId = (String) getEm()
                  .createQuery(
                           "select p.id from "
                                    + RichContent.class.getSimpleName()
                                    + " p where p.highlight = :STATUS ")
                  .setParameter("STATUS", true).setMaxResults(1)
                  .getSingleResult();
         return fetch(retId);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return findLast();
      }
   }

   @SuppressWarnings("unchecked")
   public void refreshEvidenza(String id)
   {
      List<RichContent> ret = null;
      try
      {
         ret = (List<RichContent>) getEm()
                  .createQuery(
                           "select p from "
                                    + RichContent.class.getSimpleName()
                                    + " p where p.id != :ID AND p.highlight = :STATUS")
                  .setParameter("ID", id).setParameter("STATUS", true)
                  .getResultList();
         if (ret != null)
         {
            for (RichContent richContent : ret)
            {
               richContent.setHighlight(false);
               update(richContent);
            }
         }

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   @SuppressWarnings("unchecked")
   public Image findHighlightImage()
   {
      try
      {
         List<RichContent> nl = getEm()
                  .createQuery(
                           "select p from "
                                    + RichContent.class.getSimpleName()
                                    + " p where p.highlight = :STATUS")
                  .setParameter("STATUS", true).getResultList();
         if (nl == null || nl.size() == 0 || nl.get(0).getImages() == null
                  || nl.get(0).getImages().size() == 0)
         {
            return null;
         }
         return nl.get(0).getImages().get(0);

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "date desc";
   }

   @Override
   public int getListSize(Search<RichContent> search)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = true;
      // the native query
      StringBuffer string_query = getListNative(search, params, count, 0, 0);
      Query query = getEm().createNativeQuery(string_query.toString());
      // substituition of parameters
      for (String param : params.keySet())
      {
         query.setParameter(param, params.get(param));
      }
      // result extraction
      return ((BigInteger) query.getSingleResult()).intValue();
   }

   @Override
   public List<RichContent> getList(Search<RichContent> search, int startRow,
            int pageSize)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = false;
      // the native query
      StringBuffer stringbuffer_query = getListNative(search, params, count, startRow, pageSize);
      Query query = getEm().createNativeQuery(stringbuffer_query.toString());
      // substituition of parameters
      for (String param : params.keySet())
      {
         query.setParameter(param, params.get(param));
      }
      // result extraction
      return extract(query.getResultList());
   }

   /**
    * In case of a main table with one-to-many collections to fetch at once
    * 
    * we need an external query to read results and an internal query to apply parameters and paginate results
    * 
    * we need just the external query to apply parameters and count the overall distinct results
    */
   protected StringBuffer getListNative(Search<RichContent> search, Map<String, Object> params, boolean count,
            int startRow, int pageSize)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String richContentAlias = "R";
      String richContentTypeAlias = "RT";

      // query string buffer
      StringBuffer sb = new StringBuffer(
               "SELECT ");

      if (count)
      {
         // we only count distinct results in case of count = true
         sb.append(" count( distinct ").append(pageAlias).append(".id ) ");
      }
      else
      {
         // we select a cartesian product of master/details rows in case of count = false
         sb.append(pageAlias).append(".id, ");
         sb.append(pageAlias).append(".title, ");
         sb.append(richContentAlias).append(".author, ");
         sb.append(richContentAlias).append(".content, ");
         sb.append(richContentAlias).append(".date, ");
         sb.append(richContentAlias).append(".highlight, ");
         sb.append(richContentAlias).append(".preview, ");
         sb.append(richContentAlias).append(".tags,  ");
         sb.append(richContentAlias).append(".richContentType_id, ");
         sb.append(richContentTypeAlias).append(".name AS richContentType, ");
         sb.append(" I.fileName AS image, ");
         sb.append(" D.filename AS document ");
      }

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(RichContent.TABLE_NAME).append(" AS ").append(richContentAlias);
      sb.append(" LEFT JOIN ").append(RichContentType.TABLE_NAME).append(" AS ").append(richContentTypeAlias)
               .append(" ON ( ").append(richContentTypeAlias).append(".id = ").append(richContentAlias)
               .append(".richContentType_id ) ");
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(richContentAlias).append(".id = ")
               .append(pageAlias).append(".id ) ");

      if (count)
      {
         // we optimize the count query by avoiding useless left joins
      }
      else
      {
         // we need details-from clause in case of count = false
         if (RichContent.HAS_DETAILS)
         {
            sb.append(" LEFT JOIN RichContent_Document AS RD ON ( RD.RichContent_id = ").append(richContentAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN Document AS D ON ( RD.documents_id = D.id ) ");
            sb.append(" LEFT JOIN RichContent_Image AS RI ON ( RI.RichContent_id = ").append(richContentAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN Image as I on ( I.id = RI.images_id ) ");
         }
      }

      String innerPageAlias = null;
      String innerRichContentAlias = null;
      String innerRichContentTypeAlias = null;
      if (count)
      {
         // we don't need an inner query in case of count = true (because we only need distinct id to count,
         // disregarding result pagination) so aliases stay the same
         innerPageAlias = pageAlias;
         innerRichContentAlias = richContentAlias;
         innerRichContentTypeAlias = richContentTypeAlias;
      }
      else if (RichContent.HAS_DETAILS)
      {
         // we need different aliases for the inner query in case of count = false or multiple detail rows for each
         // master
         innerPageAlias = "P1";
         innerRichContentAlias = "R1";
         innerRichContentTypeAlias = "RT1";
         // inner query comes as an inner join, because mysql does not support limit in subquerys
         sb.append(" INNER JOIN ");

         // this is the opening bracket for the internal query
         sb.append(" ( ");

         sb.append(" select distinct ").append(innerPageAlias)
                  .append(".id from ");
         sb.append(RichContent.TABLE_NAME).append(" AS ").append(innerRichContentAlias);
         sb.append(" LEFT JOIN ").append(RichContentType.TABLE_NAME).append(" AS ").append(innerRichContentTypeAlias)
                  .append(" ON ( ").append(innerRichContentTypeAlias).append(".id = ").append(innerRichContentAlias)
                  .append(".richContentType_id ) ");
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerPageAlias).append(" on ( ")
                  .append(innerRichContentAlias).append(".id = ")
                  .append(innerPageAlias).append(".id ) ");
      }
      else
      {
         // we also don't need an inner query in case of master-data that has no multiple details
         // so aliases can stay the same
         innerPageAlias = pageAlias;
         innerRichContentAlias = richContentAlias;
         innerRichContentTypeAlias = richContentTypeAlias;
      }

      // we append filters right after the latest query, so that they apply to the external one in case count = true and
      // to the internal one in case count = false
      String separator = " where ";
      applyRestrictionsNative(search, innerPageAlias, innerRichContentAlias, innerRichContentTypeAlias, separator, sb,
               params);

      if (count)
      {
         // if we only need to count distinct results, we are over!
      }
      else
      {
         // we need to sort internal results to apply pagination
         sb.append(" order by ").append(innerRichContentAlias).append(".date desc ");
         // we apply limit-clause only if we want pagination
         if (pageSize > 0)
         {
            sb.append(" limit ").append(startRow).append(", ").append(pageSize).toString();
         }

         if (RichContent.HAS_DETAILS)
         {
            // this is the closing bracket for the internal query
            sb.append(" ) ");
            // this is where external id selection applies
            sb.append(" as IN2 ON ").append(pageAlias).append(".ID = IN2.ID ");
            // we also need to sort external results to keep result order within each results page
            sb.append(" order by ").append(richContentAlias).append(".date desc ");
         }

      }
      return sb;
   }

   protected void applyRestrictionsNative(Search<RichContent> search, String pageAlias, String richContentAlias,
            String richContentTypeAlias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {

      // ACTIVE TYPE
      if (true)
      {
         sb.append(separator).append(richContentTypeAlias).append(".active = :activeContentType ");
         params.put("activeContentType", true);
         separator = " and ";
      }

      // TYPE BY NAME
      if (search.getObj().getRichContentType() != null
               && search.getObj().getRichContentType().getName() != null
               && search.getObj().getRichContentType().getName().length() > 0)
      {
         sb.append(separator).append(richContentTypeAlias).append(".name = :NAMETYPE ");
         params.put("NAMETYPE", search.getObj().getRichContentType()
                  .getName());
         separator = " and ";
      }

      // TYPE BY ID
      if (search.getObj().getRichContentType() != null
               && search.getObj().getRichContentType().getId() != null)
      {
         sb.append(separator).append(richContentTypeAlias).append(".id = :IDTYPE ");
         params.put("IDTYPE", search.getObj().getRichContentType().getId());
         separator = " and ";
      }

      // TAG
      if (search.getObj().getTag() != null
               && search.getObj().getTag().trim().length() > 0)
      {
         {
            String tagName = search.getObj().getTag().trim();

            // TODO better - this is a hack to overcome strange characters in the search form fields (i.e.: Forlì)
            String tagNameCleaned = StringUtils.clean(
                     search.getObj().getTag().trim()).replace('-', ' ');
            Boolean likeMatch = null;
            if (!tagName.equals(tagNameCleaned))
            {
               // if tagName and tagNameCleaned are not the same we perform like-match with tagNameCleaned, to prevent
               // no-results
               likeMatch = true;
            }
            else
            {
               // otherwise we can do perfect-match with the original tagName
               likeMatch = false;
            }

            sb.append(separator).append(richContentAlias).append(".id in ( ");
            sb.append(" select distinct T1.richContent_id from ")
                     .append(Tag.TABLE_NAME)
                     .append(" T1 where T1.tagName ")
                     .append(likeMatch ? "like" : "=").append(" :TAGNAME ");
            sb.append(" ) ");
            params.put("TAGNAME", likeMatch ? likeParam(tagNameCleaned)
                     : tagName);
            separator = " and ";
         }
      }

      // TAG LIKE
      if (search.getObj().getTagList().size() > 0)
      {
         sb.append(separator).append(" ( ");
         for (int i = 0; i < search.getObj().getTagList().size(); i++)
         {
            sb.append(i > 0 ? " or " : "");

            // TODO benchmark - try which version runs faster
            boolean useJoin = false;
            if (useJoin)
            {
               sb.append(richContentAlias).append(".id in ( ");
               sb.append(" select distinct T2.richContent_id from ")
                        .append(Tag.TABLE_NAME)
                        .append(" T2 where upper ( T2.tagName ) like :TAGNAME")
                        .append(i).append(" ");
               sb.append(" ) ");
            }
            else
            {
               sb.append(" upper ( ").append(richContentAlias).append(".tags ) like :TAGNAME").append(i)
                        .append(" ");
            }

            params.put("TAGNAME" + i, likeParam(search.getObj().getTag()
                     .trim().toUpperCase()));
         }
         separator = " and ";
      }

      // TITLE --> ALSO SEARCH IN CUSTOM FIELDS
      String customLike = null;
      if (search.getObj().getTitle() != null
               && !search.getObj().getTitle().trim().isEmpty())
      {
         customLike = "upper ( " + richContentAlias + ".content like :LIKETEXTCUSTOM ";
         params.put("LIKETEXTCUSTOM", search.getObj().getTitle().trim());
      }
      super.applyRestrictionsNative(search, pageAlias, separator, sb, params, customLike);

   }

   /**
    * // we select a cartesian product of master/details rows in case of count = false
    * sb.append(pageAlias).append(".id, "); sb.append(pageAlias).append(".title, ");
    * sb.append(richContentAlias).append(".author, "); sb.append(richContentAlias).append(".content, ");
    * sb.append(richContentAlias).append(".date, "); sb.append(richContentAlias).append(".highlight, ");
    * sb.append(richContentAlias).append(".preview, "); sb.append(richContentAlias).append(".tags,  ");
    * sb.append(richContentAlias).append(".richContentType_id, ");
    * sb.append(richContentTypeAlias).append(".name AS richContentType, "); sb.append(" I.fileName AS image, ");
    * sb.append(" D.filename AS document ");
    * 
    * @param resultList
    * @return
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   protected List<RichContent> extract(List resultList)
   {
      RichContent richContent = null;
      Map<String, Set<String>> imageNames = new HashMap<String, Set<String>>();
      Map<String, Set<String>> documentNames = new HashMap<String, Set<String>>();
      Map<String, RichContent> richContents = new HashMap<String, RichContent>();

      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         richContent = new RichContent();
         Object[] row = results.next();
         int i = 0;
         String id = (String) row[i];
         // if (id != null && !id.isEmpty())
         richContent.setId(id);
         i++;
         String title = (String) row[i];
         // if (title != null && !title.isEmpty())
         richContent.setTitle(title);
         i++;
         String author = (String) row[i];
         // if (author != null && !author.isEmpty())
         richContent.setAuthor(author);
         i++;
         String content = (String) row[i];
         // if (content != null && !content.isEmpty())
         richContent.setContent(content);
         i++;
         Timestamp date = (Timestamp) row[i];
         if (date != null)
         {
            richContent.setDate(new Date(date.getTime()));
         }
         i++;
         Object hightlight = row[i];
         if (hightlight != null)
         {
            if (hightlight instanceof Short)
            {
               richContent.setHighlight(((Short) hightlight) > 0 ? true : false);
            }
            else if (hightlight instanceof Boolean)
            {
               richContent.setHighlight(((Boolean) hightlight).booleanValue());
            }
            else
            {
               logger.error("hightlight instance of " + hightlight.getClass().getCanonicalName());
            }
         }
         i++;
         String preview = (String) row[i];
         // if (preview != null && !preview.isEmpty())
         richContent.setPreview(preview);
         i++;
         String tags = (String) row[i];
         // if (tags != null && !tags.isEmpty())
         richContent.setTags(tags);
         i++;
         Object richContentType_id = row[i];
         if (richContentType_id != null)
         {
            if (richContentType_id instanceof BigInteger)
            {
               richContentType_id = (BigInteger) row[i];
               richContent.getRichContentType().setId(((BigInteger) richContentType_id).longValue());
            }
            else
            {
               logger.error("richContentType_id instance of " + richContentType_id.getClass().getCanonicalName());
            }
         }
         else
         {
            logger.error("richContentType_id should not be null");
         }
         i++;
         String richContentType = (String) row[i];
         if (richContentType != null && !richContentType.isEmpty())
            richContent.getRichContentType().setName(richContentType);
         i++;
         String imagefileName = (String) row[i];
         if (imagefileName != null && !imagefileName.isEmpty())
         {
            if (imageNames.containsKey(id))
            {
               HashSet<String> set = (HashSet<String>) imageNames.get(id);
               set.add(imagefileName);
            }
            else
            {
               HashSet<String> set = new HashSet<String>();
               set.add(imagefileName);
               imageNames.put(id, set);
            }
         }
         i++;
         String documentfileName = (String) row[i];
         if (documentfileName != null && !documentfileName.isEmpty())
         {
            if (documentNames.containsKey(id))
            {
               HashSet<String> set = (HashSet<String>) documentNames.get(id);
               set.add(documentfileName);
            }
            else
            {
               HashSet<String> set = new HashSet<String>();
               set.add(documentfileName);
               documentNames.put(id, set);
            }
         }
         if (!richContents.containsKey(id))
         {
            richContents.put(id, richContent);
         }

      }
      for (String id : documentNames.keySet())
      {
         RichContent rich = null;
         if (richContents.containsKey(id))
         {
            rich = richContents.get(id);
            Set<String> docs = documentNames.get(id);
            for (String docFileName : docs)
            {
               Document document = new Document();
               document.setFilename(docFileName);
               rich.addDocument(document);
            }
         }
         else
         {
            logger.error("ERROR - DOCS CYCLE cannot find id:" + id);
         }

      }
      for (String id : imageNames.keySet())
      {
         RichContent rich = null;
         if (richContents.containsKey(id))
         {
            rich = richContents.get(id);
            Set<String> docs = imageNames.get(id);
            for (String imgFileName : docs)
            {
               Image image = new Image();
               image.setFilename(imgFileName);
               rich.addImage(image);
            }
         }
         else
         {
            logger.error("ERROR - IMGS CYCLE cannot find id:" + id);
         }

      }
      return new ArrayList<RichContent>(richContents.values());
   }

   @Override
   public RichContent fetch(Object key)
   {
      RichContent richContent = null;
      Set<String> imageNames = new HashSet<String>();
      Set<String> documentNames = new HashSet<String>();

      String nativeQuery = "SELECT  P.id, P.title, R.author, R.content, R.date, R.highlight, R.preview, R.tags, R.richContentType_id, RT.name as richContentType,"
               + "I.fileName as image, "
               + "D.filename as document "
               + "FROM `RichContent` as R "
               + "left join RichContentType as RT on (RT.id=R.richContentType_id) "
               + "left join Page as P on (R.id=P.id) "
               + "left join RichContent_Document as RD on (RD.RichContent_id=R.id) "
               + "left join Document as D on (RD.documents_id=D.id) "
               + "left join RichContent_Image as RI on (RI.RichContent_id=R.id) "
               + "left join Image as I on (I.id=RI.images_id) "
               + "where R.id= :ID";
      @SuppressWarnings("unchecked")
      Iterator<Object[]> results = getEm()
               .createNativeQuery(nativeQuery).setParameter("ID", key).getResultList().iterator();
      while (results.hasNext())
      {
         if (richContent == null)
            richContent = new RichContent();
         Object[] row = results.next();
         int i = 0;
         String id = (String) row[i];
         if (id != null && !id.isEmpty())
            richContent.setId(id);
         i++;
         String title = (String) row[i];
         if (title != null && !title.isEmpty())
            richContent.setTitle(title);
         i++;
         String author = (String) row[i];
         if (author != null && !author.isEmpty())
            richContent.setAuthor(author);
         i++;
         String content = (String) row[i];
         if (content != null && !content.isEmpty())
            richContent.setContent(content);
         i++;
         Timestamp date = (Timestamp) row[i];
         if (date != null)
         {
            richContent.setDate(new Date(date.getTime()));
         }
         i++;
         if (row[i] != null && row[i] instanceof Short)
         {
            richContent.setHighlight(((Short) row[i]) > 0 ? true : false);
         }
         else if (row[i] != null && row[i] instanceof Boolean)
         {
            richContent.setHighlight(((Boolean) row[i]).booleanValue());
         }
         i++;
         String preview = (String) row[i];
         if (preview != null && !preview.isEmpty())
            richContent.setPreview(preview);
         i++;
         String tags = (String) row[i];
         if (tags != null && !tags.isEmpty())
            richContent.setTags(tags);
         i++;
         BigInteger richContentType_id = null;
         if (row[i] instanceof BigInteger)
         {
            richContentType_id = (BigInteger) row[i];
            richContent.getRichContentType().setId(richContentType_id.longValue());
         }
         i++;
         String richContentType = (String) row[i];
         if (richContentType != null && !richContentType.isEmpty())
            richContent.getRichContentType().setName(richContentType);
         i++;
         String imagefileName = (String) row[i];
         if (imagefileName != null && !imagefileName.isEmpty())
         {
            imageNames.add(imagefileName);
         }
         i++;
         String documentfileName = (String) row[i];
         if (documentfileName != null && !documentfileName.isEmpty())
         {
            documentNames.add(documentfileName);
         }

      }
      for (String doc : documentNames)
      {
         Document document = new Document();
         document.setFilename(doc);
         richContent.addDocument(document);
      }
      for (String img : imageNames)
      {
         Image image = new Image();
         image.setFilename(img);
         richContent.addImage(image);
      }
      return richContent;
   }

}
