/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator
{
   private String user, password;

   public SMTPAuthenticator(String user, String password)
   {
      this.user = user;
      this.password = password;
   }

   public PasswordAuthentication getPasswordAuthentication()
   {
      return new PasswordAuthentication(this.user, this.password);
   }
}
