package org.giavacms.richnews10importer.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.ListPage;
import org.giavacms.richnews10importer.service.Richnews10ImporterService;

@Named
@SessionScoped
public class Richnews10ImporterController implements Serializable
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @ListPage
   public static String LIST = "/private/richnews10importer/import.xhtml";

   // --------------------------------------------------------

   @Inject
   Richnews10ImporterService richnews10ImporterService;

   // --------------------------------------------------------

   public Richnews10ImporterController()
   {
   }

   public void doImport()
   {
      try
      {
         richnews10ImporterService.doImport();
         addFacesMessage("Successo");
      }
      catch (Throwable t)
      {
         t.printStackTrace();
         if (t.getMessage() != null && t.getMessage().length() > 0)
         {
            addFacesMessage(t.getMessage());
         }
         else
         {
            addFacesMessage(t.getClass().getCanonicalName());
         }
      }
   }

   protected void addFacesMessage(String summary, String message)
   {
      addFacesMessage(null, summary, message, "");
   }

   protected void addFacesMessage(String summary)
   {
      addFacesMessage(null, summary, summary, "");
   }

   protected void addFacesMessage(Severity severity, String summary,
            String message, String forComponentId)
   {
      FacesMessage fm = new FacesMessage(message);
      fm.setSummary(summary);
      if (severity != null)
      {
         fm.setSeverity(severity);
      }
      else
      {
         fm.setSeverity(FacesMessage.SEVERITY_ERROR);
      }
      FacesContext.getCurrentInstance().addMessage(forComponentId, fm);
   }

}
