package org.giavacms.resource.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
   private String content;

   public Resource()
   {
   }

   @JsonIgnore
   public Resource(File file)
   {
      this.name = file.getName();
      this.folder = file.getParent();
      this.path = file.getAbsolutePath();
      this.type = ResourceUtils.getType(file.getAbsolutePath());
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

   public String getContent()
   {
      return content;
   }

   public void setContent(String content)
   {
      this.content = content;
   }
}
