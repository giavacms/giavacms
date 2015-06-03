import org.junit.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by fiorenzo on 31/05/15.
 */

public class PathTest
{
   @Test
   public void test() throws Exception
   {
      Path toRead = FileSystems.getDefault()
               .getPath("/Users/fiorenzo/IdeaProjects/giavacms/resources/src/test", "resources", "prova.txt");
      System.out.println(new String(Files.readAllBytes(toRead)));

   }

   @Test
   public void relativeTest()
   {
      String path = "/Users/fiorenzo/jboss/wildfly-9.0.0.CR1/standalone/deployments/ROOT.war/";
      File file = new File("/Users/fiorenzo/jboss/wildfly-9.0.0.CR1/standalone/deployments/ROOT.war/META-INF/ancora/poi");

      String folder = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(path) + path.length());
      System.out.println(folder);
   }
}
