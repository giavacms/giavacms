/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils
{
   public static boolean isValidEmailAddress(String emailAddress)
   {
      String expression = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]";
      CharSequence inputStr = emailAddress;
      Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(inputStr);
      return matcher.matches();
   }

}
