/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.module;

import java.io.Serializable;

public class MenuValue implements Serializable
{

   private static final long serialVersionUID = 1L;

   public MenuValue()
   {

   }

   public MenuValue(String label, String value)
   {
      this.label = label;
      this.value = value;
   }

   private String label;
   private String value;

   public String getLabel()
   {
      return label;
   }

   public void setLabel(String label)
   {
      this.label = label;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }
}
