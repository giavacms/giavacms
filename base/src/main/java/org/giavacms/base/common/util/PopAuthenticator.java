/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PopAuthenticator extends Authenticator
{
   private String user;
   private String password;

   public PopAuthenticator(String user, String password)
   {
      this.user = user;
      this.password = password;
   }

   public PasswordAuthentication getPasswordAuthentication()
   {
      return new PasswordAuthentication(user, password);
   }
}
