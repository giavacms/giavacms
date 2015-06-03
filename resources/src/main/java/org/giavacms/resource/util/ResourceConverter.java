package org.giavacms.resource.util;

import org.giavacms.base.model.pojo.Resource;
import org.giavacms.base.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiorenzo on 29/05/15.
 */
public class ResourceConverter
{

   public static List<Resource> fromNames(String folder, List<String> files) throws Exception
   {
      String root = ResourceUtils.getRoot();
      List<Resource> resources = new ArrayList<>();
      for (String name : files)
      {
         Resource resource = new Resource(new File(folder, name), root);
         resources.add(resource);
      }
      return resources;
   }
}
