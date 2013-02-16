/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.util;

import org.giavacms.base.common.util.JSFLocalUtils;

public class MenuUtils
{

   public static java.lang.Boolean showMenu(String open, String nomeMenu)
   {
      if (open != null && open.length() > 0)
         return Boolean.parseBoolean(open);
      else
         return JSFLocalUtils.urlContains(nomeMenu);
   }

}
