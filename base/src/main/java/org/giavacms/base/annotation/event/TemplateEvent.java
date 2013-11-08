package org.giavacms.base.annotation.event;

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
