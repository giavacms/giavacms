/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ImportController
{

   public static void main(String[] args)
   {
      Configuration cfg2 = new Configuration();
      SchemaExport schemaExport2 = new SchemaExport(cfg2);
      schemaExport2.setImportFile("/import-2.sql");
      schemaExport2.create(false, true);
   }
}
