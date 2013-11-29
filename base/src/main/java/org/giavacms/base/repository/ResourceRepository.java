/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.pojo.Resource;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class ResourceRepository extends AbstractRepository<Resource>
{

   private static final long serialVersionUID = 1L;

   // private static String base = null;

   @PersistenceContext
   EntityManager em;

   // static {
   // base = ResourceRepository.class.getClassLoader()
   // .getResource("META-INF").getPath();
   // logger.info("base: " + base);
   // if (base != null && base.contains("WEB-INF"))
   // base = base.substring(0, base.indexOf("WEB-INF"));
   //
   // // +"risorse"+File.separator
   //
   // logger.info("Saving resources to: " + base);
   // }

   @Override
   public Resource persist(Resource object)
   {
      try
      {
         String filename = null;
         if (ResourceType.IMAGE.equals(object.getResourceType()))
         {
            filename = ResourceUtils.createImage_(ResourceUtils.getFolder(object.getName()),
                     object.getName(), object.getBytes());
         }
         else
         {
            filename = ResourceUtils.createFile_(ResourceUtils.getFolder(object.getName()),
                     object.getName(), object.getBytes());
         }
         object.setName(filename);
         object.setId(object.getType() + File.separator + object.getName());
         return object;
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         return null;
      }
   }

   public Resource find(String tipo, String id)
   {
      try
      {
         File f = new File(ResourceUtils.getRealPath() + tipo + File.separator
                  + id);
         if (f.exists() && !f.isDirectory())
         {
            Resource r = new Resource();
            r.setId(id);
            r.setName(id);
            r.setType(tipo);
            return r;
         }
         else
         {
            return null;
         }
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         return null;
      }
   }

   public Resource fetch(String tipo, String id)
   {
      FileInputStream fis = null;
      try
      {
         File f = new File(ResourceUtils.getRealPath() + tipo + File.separator
                  + id);
         if (f.exists() && !f.isDirectory())
         {
            Resource r = new Resource();
            r.setId(id);
            r.setName(id);
            r.setType(tipo);
            fis = new FileInputStream(f);
            byte[] bytes = new byte[(int) f.length()];
            for (int i = 0; i < bytes.length; i++)
            {
               fis.read(bytes);
            }
            r.setBytes(bytes);
            return r;
         }
         else
         {
            return null;
         }
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         return null;
      }
      finally
      {
         if (fis != null)
         {
            try
            {
               fis.close();
            }
            catch (Exception e)
            {
            }
         }
      }
   }

   public Resource updateResource(Resource object)
   {
      try
      {
         File f = new File(ResourceUtils.getRealPath() + object.getType()
                  + File.separator + object.getId());
         if (f.exists() && f.isDirectory())
            throw new Exception("file could not be written!");
         if (f.exists())
            f.delete();
         f = new File(ResourceUtils.getRealPath() + object.getType()
                  + File.separator + object.getId());
         FileOutputStream fos = new FileOutputStream(f);
         fos.write(object.getBytes());
         fos.close();
         return object;
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         return null;
      }
   }

   public void delete(Resource object)
   {
      try
      {
         File f = new File(ResourceUtils.getRealPath() + object.getType()
                  + File.separator + object.getId());
         if (f.exists() && f.isDirectory())
            throw new Exception("file could not be deleted: "
                     + f.getAbsolutePath());
         if (f.exists())
            f.delete();
         return;
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         return;
      }
   }

   // --- LIST ------------------------------------------
   @Override
   public List<Resource> getList(Search<Resource> ricerca, int startRow,
            int pageSize)
   {
      List<Resource> result = new ArrayList<Resource>();
      try
      {
         // File f = new
         // File(base+File.separator+ricerca.getOggetto().getType());
         // if ( ! f.exists() || ! f.isDirectory() )
         // throw new Exception("directory could not be read!");
         List<String> files = getFiles(ricerca.getObj().getType(), ricerca
                  .getObj().getName());
         if (startRow > files.size())
            return result;
         int max = files.size();
         if (pageSize > 0)
            max = startRow + pageSize;
         if (max > files.size())
         {
            max = files.size();
         }
         for (int i = startRow; i < max; i++)
         {
            Resource r = new Resource();
            r.setType(ricerca.getObj().getType());
            // r.setId( files[i].substring( files[i].lastIndexOf("/")+1 ) );
            r.setId(files.get(i));
            r.setName(r.getId());
            result.add(r);
         }
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
      }
      return result;
   }

   @Override
   public int getListSize(Search<Resource> ricerca)
   {
      try
      {
         return getFiles(ricerca.getObj().getType(),
                  ricerca.getObj().getName()).size();
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         return 0;
      }
   }

   private List<String> getFiles(String tipo, String nameLike)
   {
      List<String> resources = new ArrayList<String>();
      ResourceType resourceType = null;
      try
      {
         resourceType = ResourceType.getValueByFolder(tipo);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
      if (resourceType == null)
      {
         return resources;
      }
      switch (resourceType)
      {
      case ALL:
         return resources;
      default:
         resources = ResourceUtils.getFilesName(resourceType.getFolder(), resourceType.getExtensions());
         break;
      }
      if (resources == null || resources.size() == 0 || nameLike == null
               || nameLike.equals(""))
         return resources;
      List<String> filteredFiles = new ArrayList<String>();
      for (String filename : resources)
      {
         if (filename.toUpperCase().contains(nameLike.toUpperCase()))
            filteredFiles.add(filename);
      }
      return filteredFiles;
   }

   @Override
   public EntityManager getEm()
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
      return "nome";
   }

   public boolean createSubFolder(ResourceType resourceType, String subfolderName)
   {
      String fullpath = ResourceUtils.getRealPath() + resourceType.getFolder() + File.separator + subfolderName;
      File newPath = new File(fullpath);
      if (newPath.exists() && newPath.isDirectory())
      {
         return true;
      }
      else
      {
         return newPath.mkdir();
      }
   }

}
