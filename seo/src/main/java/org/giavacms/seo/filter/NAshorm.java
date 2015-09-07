package org.giavacms.seo.filter;

import org.apache.commons.io.IOUtils;

import javax.script.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by fiorenzo on 19/08/15.
 */
public class Nashorm
{

   private static final String ZXCVBN_PATH = "src/main/resources/angular.min.js";

   public static void main(String[] args) throws ScriptException
   {
      ScriptEngineManager factory = new ScriptEngineManager();
      // create JavaScript engine
      ScriptEngine engine = factory.getEngineByName("JavaScript");

      //bind main instance
      Bindings bindings = engine.createBindings();
      bindings.put("main", "");
      engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
      try
      {
         engine.eval(ZXCVBN_PATH);
      }
      catch (ScriptException e)
      {
         throw new RuntimeException(e);
      }
      engine.eval("print('Hello World!');");

   }

   private static String getResourceContents(String path)
   {
      InputStream in = Nashorm.class.getResourceAsStream(path);
      if (in == null)
      {
         throw new RuntimeException("Resource '" + path + "' cannot be found.");
      }

      try
      {
         return IOUtils.toString(in, String.valueOf(StandardCharsets.UTF_8));
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      finally
      {
         IOUtils.closeQuietly(in);
      }
   }
}
