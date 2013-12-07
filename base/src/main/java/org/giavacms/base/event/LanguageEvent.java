/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.event;

public class LanguageEvent
{
   Long templateImplId;
   int lang;
   boolean set;

   public LanguageEvent(Long templateImplId, int lang, boolean set)
   {
      super();
      this.templateImplId = templateImplId;
      this.lang = lang;
      this.set = set;
   }

   public LanguageEvent(Long templateImplId, Long lang, boolean set)
   {
      this(templateImplId, lang.intValue(), set);
   }

   public Long getTemplateImplId()
   {
      return templateImplId;
   }

   public int getLang()
   {
      return lang;
   }

   public boolean isSet()
   {
      return set;
   }

}
