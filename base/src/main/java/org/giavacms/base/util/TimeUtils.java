/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils
{
   public static Date adjustHourAndMinute(Date n)
   {
      if (n == null)
         n = new Date();
      Calendar cal = Calendar.getInstance();
      int hour = cal.get(Calendar.HOUR_OF_DAY);
      int minutes = cal.get(Calendar.MINUTE);
      cal.setTime(n);
      cal.set(Calendar.MINUTE, minutes);
      cal.set(Calendar.HOUR_OF_DAY, hour);
      return cal.getTime();
   }
}
