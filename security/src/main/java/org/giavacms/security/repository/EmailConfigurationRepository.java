/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.security.repository;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.security.model.EmailConfiguration;

@Named
@Stateless
public class EmailConfigurationRepository extends BaseRepository<EmailConfiguration> implements
         Serializable
{

   private static final long serialVersionUID = 1L;

   public EmailConfiguration getEmailConfiguration()
   {
      try
      {
         return getEm().find(EmailConfiguration.class, 1L);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }


   public EmailConfiguration load() throws Exception
   {
      EmailConfiguration c = null;
      try
      {
         c = find(1L);
      }
      catch (Exception e)
      {
      }
      if (c == null)
      {
         c = new EmailConfiguration();
         persist(c);
      }
      return c;
   }
}
