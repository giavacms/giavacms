/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.request;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.base.annotation.Languages;
import org.giavacms.base.model.Language;
import org.giavacms.base.pojo.I18nParams;
import org.jboss.logging.Logger;


@RequestScoped
public class I18nProducer implements Serializable
{

   Logger logger = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;

   @Inject
   @Languages
   List<Language> languages;

   private I18nParams i18nParams;

   @Produces
   @Named
   public I18nParams getI18nParams()
   {
      if (i18nParams != null)
      {
         logger.info("i18nParams gia' inizializzati");
         return i18nParams;
      }
      HttpServletRequest request = (HttpServletRequest) FacesContext
               .getCurrentInstance().getExternalContext().getRequest();
      Map<String, String[]> parameters = request.getParameterMap();
      i18nParams = new I18nParams(languages.toArray(new Language[] {}));
      // don't know which language is current one. so each map is cloned (to
      // make maps independent of each other) from the initial request
      // parameters
      for (String name : parameters.keySet())
      {
         languages: for (Language language : languages)
         {
            if (language == null || !language.isEnabled())
            {
               continue languages;
            }
            String[] valuesArray = parameters.get(name);
            // strings are cloned by default, arrays are exchanged by
            // reference. thus arrays have to be cloned
            String[] valuesArrayCloned = new String[valuesArray.length];
            for (int v = 0; v < valuesArray.length; v++)
            {
               valuesArrayCloned[v] = valuesArray[v];
            }
            i18nParams
                     .puts(language.getPosition(), name, valuesArrayCloned);
         }
      }
      return i18nParams;
   }
}
