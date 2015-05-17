/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ResourceType
{

   FONT(false, "caratteri", "font", Arrays.asList("otf", "eot", "svg", "ttf",
            "woff")),
   IMAGE(false, "immagini", "img", Arrays.asList("gif", "jpg", "jpeg", "png", "ico")),
   DOCUMENT(false, "documenti", "docs", Arrays.asList("zip", "gz", "tar", "bz2", "pdf", "doc", "docx", "xls", "xlsx",
            "p7m",
            "txt", "rtf")),
   STYLESHEET(true, "fogli di stile", "css", Arrays.asList("css")),
   JAVASCRIPT(true, "funzioni javascript", "js", Arrays.asList("js")),
   FLASH(false, "contenuti flash", "swf", Arrays.asList("swf")),

   STATIC(true, "contenuti statici", "h", Arrays.asList("xhtml", "html", "htm")),

   ALL(false, "tutto", "", new ArrayList<String>()),

   ;

   boolean editable;
   private String description;
   private String folder;
   private List<String> extensions;

   private ResourceType(boolean editable, String description, String folder, List<String> extensions)
   {
      this.editable = editable;
      this.description = description;
      this.folder = folder;
      this.extensions = extensions;
   }

   public String getFolder()
   {
      return folder;
   }

   public List<String> getExtensions()
   {
      return extensions;
   }

   public String getDescription()
   {
      return description;
   }

   public boolean isEditable()
   {
      return editable;
   }

   public static ResourceType getValueByFolder(String folder)
   {
      for (ResourceType res : ResourceType.values())
      {
         if (res.getFolder().equals(folder))
            return res;
      }
      return null;
   }

}
