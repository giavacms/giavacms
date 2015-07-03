package org.giavacms.chalet.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.giavacms.api.model.Search;
import org.giavacms.api.util.IdUtils;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.base.util.StringUtils;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletTag;

@Stateless
public class ChaletRepository extends BaseRepository<Chalet>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "name asc";
   }

   @SuppressWarnings("unchecked")
   public List<Image> getImages(String id)
   {
      return getEm()
               .createNativeQuery(
                        "SELECT I.id, I.active, I.description, I.filename, I.name, I.type FROM " + Image.TABLE_NAME
                                 + " I left join " + Chalet.IMAGES_JOINTABLE_NAME
                                 + " RI on (I.id = RI." + Chalet.IMAGE_FK + ") left join "
                                 + Chalet.TABLE_NAME
                                 + " RC on (RC.id=RI." + Chalet.TABLE_FK + ") "
                                 + " where RC.id = :ID and I.active = :ACTIVE",
                        Image.class).setParameter("ID", id).setParameter("ACTIVE", true)
               .getResultList();
   }

   @Override
   protected Chalet prePersist(Chalet n)
   {
      String idTitle = IdUtils.createPageId(n.getName());
      String idFinal = makeUniqueKey(idTitle, Chalet.TABLE_NAME);
      n.setId(idFinal);

      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setDescription(n.getDescription());
      return n;
   }

   @Override
   protected Chalet preUpdate(Chalet n)
   {
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setDescription(n.getDescription());
      return n;
   }

   @Override
   protected void applyRestrictions(Search<Chalet> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // TAG
      if (search.getObj().getTag() != null
               && search.getObj().getTag().trim().length() > 0)
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

         sb.append(separator).append(alias).append(".id in ( ");
         sb.append(" select T1.chaletId from ")
                  .append(ChaletTag.class.getSimpleName())
                  .append(" T1 where T1.tagName ")
                  .append(likeMatch ? "like" : "=").append(" :TAGNAME ");
         sb.append(" ) ");
         params.put("TAGNAME", likeMatch ? likeParam(tagNameCleaned.trim())
                  : tagName);
         separator = " and ";
      }

      // TAG LIKE
      if (search.getLike().getTagList().size() > 0)
      {
         sb.append(separator).append(" ( ");
         for (int i = 0; i < search.getLike().getTagList().size(); i++)
         {
            sb.append(i > 0 ? " or " : "");

            sb.append(alias).append(".id in ( ");
            sb.append(" select T2.chaletId from ")
                     .append(ChaletTag.class.getSimpleName())
                     .append(" T2 where upper ( T2.tagName ) like :TAGNAME")
                     .append(i).append(" ");
            sb.append(" ) ");

            // params.put("TAGNAME" + i, likeParam(search.getObj().getTag()
            // .trim().toUpperCase()));
            params.put("TAGNAME" + i, likeParam(search.getObj().getTagList().get(i)
                     .trim().toUpperCase()));

         }
         sb.append(" ) ");
         separator = " and ";
      }

      // NAME
      if (search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) like :likeName ");
         params.put("likeName", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }

      // licenseNumber
      if (search.getLike().getLicenseNumber() != null
               && !search.getLike().getLicenseNumber().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".licenseNumber ) like :licenseNumber ");
         params.put("licenseNumber", likeParam(search.getLike().getLicenseNumber().trim().toUpperCase()));
         separator = " and ";
      }

      // CONTENT (ALSO SEARCHES IN TITLE)
      if (search.getLike().getDescription() != null
               && !search.getLike().getDescription().trim().isEmpty())
      {
         sb.append(separator);
         sb.append(" ( ");
         sb.append(" upper ( ").append(alias).append(".title ) like :likeContent ");
         sb.append(" or ");
         sb.append(" upper ( ").append(alias).append(".description ) like :likeContent ");
         params.put("likeContent", likeParam(search.getLike().getDescription().trim().toUpperCase()));
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   @Override
   public void delete(Object key) throws Exception
   {
      getEm().createNativeQuery("update " + Chalet.TABLE_NAME + " set active = :active where id = :id")
               .setParameter("active", false).setParameter("id", key).executeUpdate();
   }

   public void addImage(String chaletId, Long imageId)
   {
      getEm().createNativeQuery(
               "INSERT INTO " + Chalet.IMAGES_JOINTABLE_NAME + "(" + Chalet.TABLE_FK + ", "
                        + Chalet.IMAGE_FK + ") VALUES (:chaletId,:imageId) ")
               .setParameter("chaletId", chaletId).setParameter("imageId", imageId).executeUpdate();
   }

   public void removeImage(String chaletId, Long imageId)
   {
      getEm().createNativeQuery(
               "DELETE FROM " + Chalet.IMAGES_JOINTABLE_NAME + " where " + Chalet.TABLE_FK
                        + " = :chaletId and "
                        + Chalet.IMAGE_FK + " = :imageId ")
               .setParameter("chaletId", chaletId).setParameter("imageId", imageId).executeUpdate();
   }

   public Map<String, Chalet> getChaletMap() throws Exception
   {
      Map<String, Chalet> map = new HashMap<>();
      List<Chalet> list = getList(new Search<Chalet>(Chalet.class), 0, 0);
      for (Chalet chalet : list)
      {
         map.put(chalet.getLicenseNumber(), chalet);
      }
      return map;
   }
}
