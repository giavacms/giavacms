/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.event;

import org.giavacms.base.model.Template;

public class TemplateEvent
{
   Template template;

   public Template getTemplate()
   {
      return template;
   }

   public TemplateEvent(Template template)
   {
      this.template = template;
   }

}
