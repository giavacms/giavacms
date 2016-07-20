package org.giavacms.mvel.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.giavacms.mvel.model.enums.MvelPlaceHolderType;
import org.giavacms.mvel.model.pojo.MvelPlaceholder;
import org.jboss.logging.Logger;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

public class MvelUtils
{

   static Logger logger = Logger.getLogger(MvelUtils.class);

   public static boolean canCompile(MvelPlaceholder phv)
   {
      {
         try
         {
            // NON FUNZIONA COSI
            // MVEL.compileExpression(phv2code(phv).toString());
         }
         catch (Exception e)
         {
         }
         try
         {
            // COSI SI
            boolean toEval = true;
            // logger.info("Begin of valorized-placeholder validation for: " + phv.getPlaceholder().getId() + " - "
            // + phv.getPlaceholder().getNome() + " = " + phv.getValore());
            CompiledTemplate compiled = TemplateCompiler.compileTemplate(phv2code(phv, toEval));
            // logger.info("Valorized-placeholder compiled");

            if (toEval)
            {
               Map<String, Object> vars = new HashMap<String, Object>();
               TemplateRuntime.execute(compiled, vars);
               logger.info("End of valorized-placeholder validation for: " + phv.getName());
            }
            return true;
         }
         catch (Exception e)
         {
            logger.info("Failure of valorized-placeholder validation for: " + phv.getName() + " = " + phv.getValues());
            if (phv.getValues().size() > 1)
            {
               for (String values : phv.getValues())
               {
                  try
                  {
                     MvelPlaceholder phvOne = new MvelPlaceholder();
                     phvOne.setValues(Arrays.asList(values));
                     boolean toEval = true;
                     CompiledTemplate compiled = TemplateCompiler.compileTemplate(phv2code(phvOne, toEval));
                     Map<String, Object> vars = new HashMap<String, Object>();
                     TemplateRuntime.execute(compiled, vars);
                  }
                  catch (Exception ex)
                  {
                     logger.info("Failure of valorized-placeholder validation for value: "
                              + phv.getName() + " = " + values);
                  }
               }
            }
         }
         return false;
      }
   }

   public static String compile(String templateContent, List<MvelPlaceholder> phvs)
   {
      StringBuffer valorizedTemplateContent = new StringBuffer();
      for (MvelPlaceholder phv : phvs)
      {
         valorizedTemplateContent.append(phv2code(phv));
      }
      valorizedTemplateContent.append(templateContent);
      logger.info("Begin of mvel compilation");
      CompiledTemplate valorizedAndCompiledTemplate = TemplateCompiler.compileTemplate(valorizedTemplateContent);
      logger.info("End of mvel compilation");

      logger.info("Begin of mvel execution");
      Map<String, Object> vars = new HashMap<String, Object>();
      String result = (String) TemplateRuntime.execute(valorizedAndCompiledTemplate, vars);
      logger.info("End of mvel execution");

      logger.info("Begin of img path correction");
      result = MvelPrintUtils.correggiImmagini(result);
      logger.info("End of img path correction");

      return result;
   }

   public static StringBuffer phv2code(MvelPlaceholder phv)
   {
      boolean toEval = false;
      return phv2code(phv, toEval);
   }

   private static StringBuffer phv2code(MvelPlaceholder phv, boolean toEval)

   {
      StringBuffer sb = new StringBuffer();
      {
         if (MvelPlaceHolderType.SINGLE.equals(phv.getType()))
         {
            sb.append("@code{\n");
            sb.append(phv.getName()).append(" = ");
            // n.b. se non si tratta di un oggetto {"n1"="v1","n2"="v2"} o ["n1"="v1","n2"="v2"] ma stringa secca ci
            // devono essere gli apicetti attorno 'v'
            sb.append(escape2mvel(phv.getValues().get(0)));
            sb.append("\n}\n");
            if (toEval)
            {
               sb.append("\n\nresult = @{").append(phv.getName()).append("}\n\n");
            }
         }
         else
         {
            sb.append("@code{\n");
            sb.append(phv.getName()).append(" = ");
            sb.append(" { ");
            if (phv.getValues().isEmpty())
            {
               sb.append(" } ");
            }
            else if (phv.getValues().size() == 1 && phv.getValues().get(0).trim().isEmpty())
            {
               sb.append(" } ");
            }
            else
            {
               StringBuffer sbv = new StringBuffer();
               for (String v : phv.getValues())
               {
                  sbv.append(",").append(escape2mvel(v));
               }
               sb.append(sbv.substring(1));
               sb.append(" } ");
            }
            sb.append("\n}\n");
            if (toEval)
            {
               sb.append("\n\nresult = @{").append(phv.getName()).append("}\n\n");
            }

         }
      }
      return sb;
   }

   private static Object escape2mvel(String value)
   {
      if (value == null)
      {
         return null;
      }
      value = value.trim();
      if (value.startsWith("{") && value.endsWith("}"))
      {
         return value;
      }
      else if (value.startsWith("[") && value.endsWith("]"))
      {
         return value;
      }
      else if (!value.startsWith("'") && !value.endsWith("'"))
      {
         if (value.startsWith("\"") && value.endsWith("\""))
         {
            return value;
         }
         else
         {
            boolean escaped = true;
            int indexOfApex = value.indexOf("'", 0);
            while (indexOfApex > 0)
            {
               if (value.charAt(indexOfApex - 1) != '\\')
               {
                  escaped = false;
                  break;
               }
               else
               {
                  indexOfApex = value.indexOf("'", indexOfApex + 1);
               }
            }
            indexOfApex = value.indexOf("\"", 0);
            while (indexOfApex > 0)
            {
               if (value.charAt(indexOfApex - 1) != '\\')
               {
                  escaped = false;
                  break;
               }
               else
               {
                  indexOfApex = value.indexOf("\"", indexOfApex + 1);
               }
            }
            if (escaped)
            {
               return "'" + value + "'";
            }
            else
            {
               // incrociamo le dita
               return value;
            }
         }
      }
      return value;
   }

   public static <T> T getItem(String url, Class<T> classT)
   {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(url))
      {
         webTargetClosable.response = webTargetClosable.webTarget.request().buildGet().invoke();
         T t = webTargetClosable.response.readEntity(classT);
         return t;
      }
      catch (Throwable e)
      {
         logger.error(e.getMessage(), e);
         try
         {
            return classT.newInstance();
         }
         catch (Throwable e1)
         {
            logger.error(e1.getMessage(), e1);
            return null;
         }
      }

   }

   @SuppressWarnings("unchecked")
   public static <T> List<T> getList(String url, Class<T> classT)
   {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(url))
      {
         webTargetClosable.response = webTargetClosable.webTarget.request().buildGet().invoke();
         List<T> l = webTargetClosable.response.readEntity(new ArrayList<T>().getClass());
         return l;
      }
      catch (Throwable e)
      {
         logger.error(e.getMessage(), e);
         try
         {
            return new ArrayList<T>();
         }
         catch (Throwable e1)
         {
            logger.error(e1.getMessage(), e1);
            return null;
         }
      }

   }
}
