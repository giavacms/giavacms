package org.giavacms.richcontent.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
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

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.controller.util.TimeUtils;
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

   /**
    * criteri di default, comuni a tutti, ma specializzabili da ogni EJB tramite overriding
    */

   @Override
   protected void applyRestrictions(Search<RichContent> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // ACTIVE TYPE
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".richContentType.active = :activeContentType ");
         params.put("activeContentType", true);
         separator = " and ";
      }

      // TYPE BY NAME
      if (search.getObj().getRichContentType() != null
               && search.getObj().getRichContentType().getName() != null
               && search.getObj().getRichContentType().getName().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".richContentType.name = :NAMETYPE ");
         params.put("NAMETYPE", search.getObj().getRichContentType()
                  .getName());
         separator = " and ";
      }

      // TYPE BY ID
      if (search.getObj().getRichContentType() != null
               && search.getObj().getRichContentType().getId() != null)
      {
         sb.append(separator).append(alias)
                  .append(".richContentType.id = :IDTYPE ");
         params.put("IDTYPE", search.getObj().getRichContentType().getId());
         separator = " and ";
      }

      // TAG
      if (search.getObj().getTag() != null
               && search.getObj().getTag().trim().length() > 0)
      {
         // try
         // {
         // params.put("TAGNAME",
         // URLEncoder.encode(search.getObj().getTag().trim(), "UTF-8"));
         // sb.append(separator).append(alias).append(".id in ( ");
         // sb.append(" select distinct rt.richContent.id from ").append(Tag.class.getSimpleName())
         // .append(" rt where rt.tagName = :TAGNAME ");
         // sb.append(" ) ");
         // separator = " and ";
         // }
         // catch (UnsupportedEncodingException e)
         {
            String tagName = search.getObj().getTag().trim();
            String tagNameCleaned = StringUtils.clean(
                     search.getObj().getTag().trim()).replace('-', ' ');
            boolean likeMatch = false;
            if (!tagName.equals(tagNameCleaned))
            {
               likeMatch = true;
            }
            sb.append(separator).append(alias).append(".id in ( ");
            sb.append(" select distinct rt.richContent.id from ")
                     .append(Tag.class.getSimpleName())
                     .append(" rt where rt.tagName ")
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

            // da provare quale versione piu' efficiente
            boolean usaJoin = false;
            if (usaJoin)
            {
               sb.append(alias).append(".id in ( ");
               sb.append(" select distinct rt.richContent.id from ")
                        .append(Tag.class.getSimpleName())
                        .append(" rt where upper ( rt.tagName ) like :TAGNAME")
                        .append(i).append(" ");
               sb.append(" ) ");
            }
            else
            {
               sb.append(" upper ( ").append(alias)
                        .append(".tags ) like :TAGNAME").append(i)
                        .append(" ");
            }

            params.put("TAGNAME" + i, likeParam(search.getObj().getTag()
                     .trim().toUpperCase()));
         }
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   @Override
   protected boolean likeSearch(String likeText, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      sb.append(separator).append(" ( ");

      sb.append(" upper ( ").append(alias).append(".title ) LIKE :LIKETEXT ");
      sb.append(" or ").append(" upper ( ").append(alias)
               .append(".content ) LIKE :LIKETEXT ");

      sb.append(" ) ");

      // params.put("LIKETEXT", StringUtils.clean(likeText));
      params.put("LIKETEXT", likeText);

      return true;
   }

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

   public RichContent findHighlight()
   {
      RichContent ret = new RichContent();
      try
      {
         ret = (RichContent) getEm()
                  .createQuery(
                           "select p from "
                                    + RichContent.class.getSimpleName()
                                    + " p where p.highlight = :STATUS ")
                  .setParameter("STATUS", true).setMaxResults(1)
                  .getSingleResult();
         for (Document document : ret.getDocuments())
         {
            document.getName();
         }

         for (Image image : ret.getImages())
         {
            image.getName();
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      if (ret == null)
         return findLast();
      return ret;
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

   public void fetchList(Object key)
   {
      RichContent richContent = null;
      Map<String, Set<String>> imageNames = new HashMap<String, Set<String>>();
      Map<String, Set<String>> documentNames = new HashMap<String, Set<String>>();
      Map<String, RichContent> richcontents = new HashMap<String, RichContent>();

      String nativeQuery = "SELECT  P.id, P.title, R.author, R.content, R.date, R.highlight, R.preview, R.tags, R.richContentType_id, RT.name as richContentType,"
               + "I.fileName as image, "
               + "D.filename as document "
               + "FROM `RichContent` as R "
               + "left join RichContentType as RT on (RT.id=R.id) "
               + "left join Page as P on (R.id=P.id) "
               + "left join RichContent_Document as RD on (RD.RichContent_id=R.id) "
               + "left join Document as D on (RD.documents_id=D.id) "
               + "left join RichContent_Image as RI on (RI.RichContent_id=R.id) "
               + "left join Image as I on (I.id=RI.images_id) "
               + "where R.id= :ID";
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
         String date = (String) row[i];
         i++;
         boolean highlight = false;
         if (row[i] != null && row[i] instanceof Short)
         {
            highlight = ((Short) row[i]) > 0 ? true : false;
         }
         else if (row[i] != null && row[i] instanceof Boolean)
         {
            highlight = ((Boolean) row[i]).booleanValue();
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
            if (imageNames.containsKey(id))
            {
               HashSet<String> set = (HashSet<String>) documentNames.get(id);
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
         if (!richcontents.containsKey(id))
         {
            richcontents.put(id, richContent);
         }

      }
      for (String id : documentNames.keySet())
      {
         RichContent rich = null;
         if (richcontents.containsKey(id))
         {
            rich = richcontents.get(id);
            Set<String> docs = documentNames.get(id);
            for (String docFileName : docs)
            {
               Document document = new Document();
               document.setFilename(docFileName);
               richContent.addDocument(document);
            }
         }
         else
         {
            System.out.println("ERRORE - DOCS CYCLE non trovo id:" + id);
         }

      }
      for (String id : imageNames.keySet())
      {
         RichContent rich = null;
         if (richcontents.containsKey(id))
         {
            rich = richcontents.get(id);
            Set<String> docs = imageNames.get(id);
            for (String imgFileName : docs)
            {
               Image image = new Image();
               image.setFilename(imgFileName);
               richContent.addImage(image);
            }
         }
         else
         {
            System.out.println("ERRORE IMGS CYCLE non trovo id:" + id);
         }

      }
      return;
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
               + "left join RichContentType as RT on (RT.id=R.id) "
               + "left join Page as P on (R.id=P.id) "
               + "left join RichContent_Document as RD on (RD.RichContent_id=R.id) "
               + "left join Document as D on (RD.documents_id=D.id) "
               + "left join RichContent_Image as RI on (RI.RichContent_id=R.id) "
               + "left join Image as I on (I.id=RI.images_id) "
               + "where R.id= :ID";
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

   @Deprecated
   public RichContent oldFetch(Object key)
   {
      try
      {
         RichContent richContent = find(key);
         for (Document document : richContent.getDocuments())
         {
            document.getName();
         }
         for (Image image : richContent.getImages())
         {
            image.getName();
         }
         return richContent;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   public RichContent getLast(String category)
   {
      Search<RichContent> r = new Search<RichContent>(RichContent.class);
      r.getObj().getRichContentType().setName(category);
      List<RichContent> list = getList(r, 0, 1);
      if (list != null && list.size() > 0)
      {
         RichContent ret = list.get(0);
         for (Document document : ret.getDocuments())
         {
            document.getName();
         }

         for (Image image : ret.getImages())
         {
            image.getName();
         }
         return ret;
      }
      return new RichContent();
   }

   @Override
   public List<RichContent> getList(Search<RichContent> ricerca, int startRow,
            int pageSize)
   {
      // TODO Auto-generated method stub
      List<RichContent> list = super.getList(ricerca, startRow, pageSize);
      for (RichContent richContent : list)
      {
         if (richContent.getImages() != null)
         {
            for (Image img : richContent.getImages())
            {
               img.getId();
               img.getFilename();
               img.getFilePath();
            }
         }
         if (richContent.getDocuments() != null)
         {
            for (Document doc : richContent.getDocuments())
            {
               doc.getId();
               doc.getFilename();
               doc.getType();
            }
         }
      }
      return list;
   }

   public void loadFirstPicture(RichContent richContent)
   {
      try
      {
         // return getEm().createNativeQuery("SELECT * FROM RichContent_Image ri " +
         // " left join Image i on (ri.images_id=i.id) " +
         // " where ri.RichContent_id in( 'fiorenzo-pizza', 'samuele-pasini')"+
         // " limit 0,1").getResultList();
         List<Image> images = getEm().merge(richContent).getImages();
         if (images != null && images.size() > 0)
         {
            richContent.setFirstImage(images.get(0));
            richContent.getFirstImage().toString();
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
      }
   }
}
