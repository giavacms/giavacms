/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.catalogue.model.Product;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class ProductRepository extends BaseRepository<Product>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected String getDefaultOrderBy()
   {
      return "code asc";
   }

   @Override
   protected void applyRestrictions(Search<Product> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // CATEGORIZED
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".category is not null ");
         separator = " and ";
      }

      // ACTIVE CATEGORY
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".category.active = :categoryActive ");
         params.put("categoryActive", true);
         separator = " and ";
      }

      // ACTIVE
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".category.active = :categoryActive ");
         params.put("categoryActive", true);
         separator = " and ";
      }

      // CATEGORY NAME
      if (search.getLike().getCategory() != null
               && search.getLike().getCategory().getName() != null
               && search.getLike().getCategory().getName().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".category.name = :categoryName ");
         params.put("categoryName", search.getLike().getCategory().getName());
         separator = " and ";
      }

      // CATEGORY ID
      if (search.getObj().getCategory() != null
               && search.getObj().getCategory().getId() != null
               && search.getObj().getCategory().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias).append(".category.id = :categoryId ");
         params.put("categoryId", search.getObj().getCategory().getId());
         separator = " and ";
      }

      // CODE
      if (search.getObj().getCode() != null
               && !search.getObj().getCode().isEmpty())
      {
         sb.append(separator).append(alias).append(".code = :CODE ");
         params.put("CODE", search.getObj().getCode());
         separator = " and ";
      }

      // VALS
      if (search.getObj().getVals() != null
               && search.getObj().getVals().size() > 0)
      {
         int valsCount = 0;
         for (String prop : search.getObj().getVals().keySet())
         {
            valsCount++;
            String[] vals = search.getObj().getVals().get(prop);
            sb.append(separator).append(" ( ");
            boolean first = true;
            for (int v = 1; v <= 10; v++)
            {

               if (!first)
               {
                  sb.append(" or ");
               }
               else
               {
                  first = false;
               }
               sb.append(" ( ");
               sb.append(alias).append(".val").append(v)
                        .append(" in ( :VAL").append(valsCount)
                        .append(" ) ");
               sb.append(" and ");
               sb.append(alias).append(".category").append(".prop").append(v)
                        .append(" = :PROP").append(valsCount);
               sb.append(" ) ");
            }
            if (valsCount != search.getObj().getVals().keySet().size())
               sb.append(separator).append(" ) ");
            else
               sb.append(" ) ");
            params.put("PROP" + valsCount, prop);
            params.put("VAL" + valsCount, Arrays.asList(vals));
            separator = " and ";
         }
      }

      // NAME
      if (search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) like :likeTitle ");
         params.put("likeTitle", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }

      // CONTENT (ALSO SEARCHES IN TITLE)
      if (search.getLike().getDescription() != null
               && !search.getLike().getDescription().trim().isEmpty())
      {
         sb.append(separator);
         sb.append(" ( ");
         sb.append(" upper ( ").append(alias).append(".name ) like :likeContent ");
         sb.append(" or ");
         sb.append(" upper ( ").append(alias).append(".description ) like :likeContent ");
         params.put("likeContent", likeParam(search.getLike().getDescription().trim().toUpperCase()));
         separator = " and ";
      }

   }

   @SuppressWarnings("unchecked")
   public List<Image> getImages(String id)
   {
      return getEm()
               .createNativeQuery(
                        "select * from " + Image.TABLE_NAME + " where id in ( "
                                 + "    select " + Product.IMAGE_FK + " from "
                                 + Product.IMAGES_JOINTABLE_NAME
                                 + " where " + Product.TABLE_FK + " = ( "
                                 + "       select id from " + Product.TABLE_NAME
                                 + "    ) "
                                 + " ) ", Image.class)
               .getResultList();
   }

   @SuppressWarnings("unchecked")
   public List<Image> getDocuments(String id)
   {
      return getEm()
               .createNativeQuery(
                        "select * from " + Document.TABLE_NAME + " where id in ( "
                                 + "    select " + Product.DOCUMENT_FK + " from "
                                 + Product.DOCUMENTS_JOINTABLE_NAME
                                 + " where " + Product.TABLE_FK + " = ( "
                                 + "       select id from " + Product.TABLE_NAME
                                 + "    ) "
                                 + " ) ", Document.class)
               .getResultList();
   }

}
