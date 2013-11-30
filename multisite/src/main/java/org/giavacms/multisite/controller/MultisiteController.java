package org.giavacms.multisite.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.ViewPage;
import org.jboss.logging.Logger;

@Named
@SessionScoped
public class MultisiteController implements Serializable
{
   Logger logger = Logger.getLogger(getClass().getName());
   private static final long serialVersionUID = 1L;
   private String properties;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml?faces-redirect=true";

   @EditPage
   public static String EDIT = "/private/multisite/edit.xhtml?faces-redirect=true";
   @ViewPage
   @ListPage
   public static String VIEW = "/private/multisite/view.xhtml?faces-redirect=true";

   // --------------------------------------------------------

   // --------------------------------------------------------

   public MultisiteController()
   {
   }

   // --------------------------------------------------------

   public String update()
   {
      store();
      return VIEW;
   }

   public String modCurrent()
   {
      load();
      return EDIT;
   }

   private void load()
   {
      try
      {
         StringBuffer stringBuffer = new StringBuffer();
         Properties properties = new Properties();
         properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("domains.properties"));
         for (String key : properties.stringPropertyNames())
         {
            String value = properties.getProperty(key);
            stringBuffer.append(key + "=" + value + "\n");
            logger.info(key + " => " + value);
         }
         this.properties = stringBuffer.toString();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   private boolean store()
   {
      Properties properties = new Properties();
      Scanner scanner = null;
      try
      {
         properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("domains.properties"));
         scanner = new Scanner(getProperties());
         while (scanner.hasNextLine())
         {
            String[] splits = scanner.nextLine().split("=");
            properties.put(splits[0], splits[1]);
         }
         URL url = Thread.currentThread().getContextClassLoader()
                  .getResource("domains.properties");
         File file = new File(url.toURI().getPath());
         properties.store(new FileOutputStream(file), "update!");

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (scanner != null)
         {
            scanner.close();
         }
      }
      return false;
   }

   public String getProperties()
   {
      if (properties == null)
         load();
      return properties;
   }

   public void setProperties(String properties)
   {
      this.properties = properties;
   }
}
