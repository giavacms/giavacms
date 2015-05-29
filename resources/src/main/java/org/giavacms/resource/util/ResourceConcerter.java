package org.giavacms.resource.util;

import org.giavacms.resource.model.pojo.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiorenzo on 29/05/15.
 */
public class ResourceConcerter
{

   public static List<Resource> fromNames(String folder, List<String> files) throws Exception
   {
      List<Resource> resources = new ArrayList<>();
      for (String name : files)
      {
         Resource resource = new Resource(new File(folder, name));
         resources.add(resource);
      }
      return resources;
   }
}
