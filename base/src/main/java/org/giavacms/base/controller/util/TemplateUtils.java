/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.util;

import org.giavacms.base.model.Template;

public class TemplateUtils
{

   public static Template clone(Template original)
   {
      Template clone = new Template();
      clone.setName(original.getName() + " - CLONE");
      clone.setActive(true);
      clone.setStatico(original.getStatico());
      clone.setCol1_start(original.getCol1_start());
      clone.setCol2_start(original.getCol2_start());
      clone.setCol3_start(original.getCol3_start());
      clone.setCol1_stop(original.getCol1_stop());
      clone.setCol2_stop(original.getCol2_stop());
      clone.setCol3_stop(original.getCol3_stop());
      clone.setHeader_start(original.getHeader_start());
      clone.setHeader_stop(original.getHeader_stop());
      clone.setFooter_start(original.getFooter_start());
      clone.setFooter_stop(original.getFooter_stop());
      return clone;
   }
}
