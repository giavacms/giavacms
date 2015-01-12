/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.producer;

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
import org.giavacms.base.pojo.I18nRequestParams;
import org.jboss.logging.Logger;

@RequestScoped
public class I18nRequestParamsProducer implements Serializable
{

   Logger logger = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;

   @Inject
   @Languages
   List<Language> languages;

   private I18nRequestParams i18nRequestParams;

   @Produces
   @Named
   public I18nRequestParams getI18nRequestParams()
   {
      if (i18nRequestParams != null)
      {
         logger.info("i18nRequestParams gia' inizializzati");
         return i18nRequestParams;
      }
      HttpServletRequest request = (HttpServletRequest) FacesContext
               .getCurrentInstance().getExternalContext().getRequest();
      Map<String, String[]> parameters = request.getParameterMap();
      i18nRequestParams = new I18nRequestParams(languages.toArray(new Language[] {}));
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
            i18nRequestParams
                     .puts(language.getPosition(), name, valuesArrayCloned);
         }
      }
      return i18nRequestParams;
   }
}
