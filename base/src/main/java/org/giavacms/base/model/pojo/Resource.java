package org.giavacms.base.model.pojo;

import org.giavacms.base.util.ResourceUtils;

import java.io.File;
import java.io.Serializable;

/**
 * Created by fiorenzo on 29/05/15.
 */
public class Resource implements Serializable
{

   private static final long serialVersionUID = 548720050279375022L;

   private String name;
   private String folder;
   private String path;
   private String type;
   private String fileContent;

   public Resource()
   {
   }

   public Resource(File file, String root)
   {
      this.name = file.getName();
      this.folder = file.getParentFile().getAbsolutePath().substring(
               file.getParentFile().getAbsolutePath().indexOf(root) + root.length());
      this.path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(root) + root.length());
      if (file.isDirectory())
      {
         this.type = "folder";
      }
      else
      {
         this.type = ResourceUtils.getType(file.getName());
      }
   }

   public Resource(File file, String root, String fileContent)
   {
      this.name = file.getName();
      this.folder = file.getParentFile().getAbsolutePath().substring(
               file.getParentFile().getAbsolutePath().indexOf(root) + root.length());
      this.path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(root) + root.length());
      if (file.isDirectory())
      {
         this.type = "folder";
      }
      else
      {
         this.type = ResourceUtils.getType(file.getName());
      }
      this.fileContent = fileContent;
   }

   public String getFolder()
   {
      return folder;
   }

   public void setFolder(String folder)
   {
      this.folder = folder;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getPath()
   {
      return path;
   }

   public void setPath(String path)
   {
      this.path = path;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getFileContent()
   {
      return fileContent;
   }

   public void setFileContent(String fileContent)
   {
      this.fileContent = fileContent;
   }
}
