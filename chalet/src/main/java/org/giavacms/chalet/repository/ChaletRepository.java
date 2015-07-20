package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.api.util.IdUtils;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.base.util.StringUtils;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletTag;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

   public List<Chalet> getAllWithImages()
   {
      logger.info("getAllWithImages");
      Map<String, Chalet> mappaChalet = new HashMap<>();
      @SuppressWarnings("unchecked")
      List<Object[]> list = getEm()
               .createNativeQuery(
                        "select C.id, C.name, C.licenseNumber, C.preview, C.description, C.tags, C.active, C.owner, C.address, C.postalNumber, "
                                 + " C.city, C.province, C.telephone, C.email, C.website, C.facebook, C.twitter, C.instagram, "
                                 + " I.id as ImageId, I.active as ImageActive, I.description  as ImageDescription, I.filename, I.name  as ImageName, I.type from "
                                 + Chalet.TABLE_NAME + " AS C "
                                 + " LEFT JOIN " + Chalet.IMAGES_JOINTABLE_NAME + " CI on (C.id=CI." + Chalet.TABLE_FK
                                 + ") "
                                 + " LEFT JOIN " + Image.TABLE_NAME + " I on (I.id = CI." + Chalet.IMAGE_FK + ") "
                                 + " where C.active = :ACTIVE1 order by C.name asc")
               .setParameter("ACTIVE1", 1)
               .getResultList();
      for (Object[] row : list)
      {
         Chalet chalet = chaletFromRow(row);
         Image image = imageFromRow(row);
         if (mappaChalet.containsKey(chalet.getId()))
         {
            if (image != null)
            {
               chalet = mappaChalet.get(chalet.getId());
               chalet.addImage(image);
            }
         }
         else
         {
            if (image != null)
            {
               chalet.addImage(image);
            }
            mappaChalet.put(chalet.getId(), chalet);
         }

      }
      return new ArrayList<>(mappaChalet.values());
   }

   private Chalet chaletFromRow(Object[] row)
   {
      int i = 0;
      Chalet c = new Chalet();
      // //C.id,
      c.setId(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //  C.name,
      c.setName(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //    C.licenseNumber,
      c.setLicenseNumber(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.preview,
      c.setPreview(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.description,
      c.setDescription(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //C.tags,
      c.setTags(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //C.active,
      c.setActive(true);
      i++;
      // C.owner
      c.setOwner(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.address,
      c.setAddress(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.postalNumber, "
      c.setPostalNumber(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.city,
      c.setCity(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.province,
      c.setProvince(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //  C.telephone,
      c.setTelephone(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //  C.email,
      c.setEmail(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.website,
      c.setWebsite(row[i] == null ? "" : row[i].toString().trim());
      i++;
      //    C.facebook,
      c.setFacebook(row[i] == null ? "" : row[i].toString().trim());
      i++;
      // C.twitter
      i++;
      // C.instagram, "
      c.setInstagram(row[i] == null ? "" : row[i].toString().trim());
      return c;
   }

   private Image imageFromRow(Object[] row)
   {
      int i = 18;
      Image c = new Image();
      //      I.id, I.active, I.description, I.filename, I.name, I.type

      if (row[i] == null)
      {
         return null;
      }
      else
      {
         c.setId(new Long(row[i].toString()));
      }
      i++;
      c.setActive(row[i] == "1" ? true : false);
      i++;
      c.setDescription(row[i] == null ? "" : row[i].toString().trim());
      i++;
      if (row[i] == null)
      {
         return null;
      }
      else
      {
         c.setFilename(row[i] == null ? "" : row[i].toString().trim());
      }

      i++;
      c.setName(row[i] == null ? "" : row[i].toString().trim());
      i++;
      c.setType(row[i] == null ? "" : row[i].toString().trim());

      return c;
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
