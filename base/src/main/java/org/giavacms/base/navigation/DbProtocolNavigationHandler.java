package org.giavacms.base.navigation;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jboss.logging.Logger;

import com.sun.faces.application.NavigationHandlerImpl;

/**
 * An extension to the default navigation handler which allows us to specify viewIds directly as the outcome. There is
 * no support for redirection, however if you need this you can specify a viewId which simply contains a web:redirect
 * tag.
 */
public class DbProtocolNavigationHandler extends NavigationHandlerImpl
{
   protected Logger log = Logger.getLogger(getClass().getName());

   /**
    * Default constructor.
    */
   public DbProtocolNavigationHandler()
   {
      super();
   }

   /**
    * Override the default navigation handler to check for viewIds in the outcome string. This simply looks for the
    * .xhtml extension.
    */
   @Override
   public void handleNavigation(FacesContext context, String fromAction,
            String outcome)
   {
      if (outcome != null && outcome.startsWith("/db:"))
      {
         // canonicalize path relative to current view
         ViewHandler views = context.getApplication().getViewHandler();
         UIViewRoot view = views.createView(context, outcome);
         context.setViewRoot(view);
      }
      else
      {
         super.handleNavigation(context, fromAction, outcome);
      }
   }
}