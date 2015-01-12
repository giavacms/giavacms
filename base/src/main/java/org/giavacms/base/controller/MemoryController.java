/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

@Named
@RequestScoped
@SuppressWarnings("serial")
public class MemoryController implements Serializable
{

   private MemoryUsage memoryUsage;
   private MemoryUsage nonMemoryUsage;
   private List<MemoryPoolMXBean> memoryPool;
   protected final Logger logger = Logger.getLogger(getClass()
            .getCanonicalName());

   public MemoryController()
   {
      // TODO Auto-generated constructor stub
   }

   @PostConstruct
   private void init()
   {
      memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
      nonMemoryUsage = ManagementFactory.getMemoryMXBean()
               .getNonHeapMemoryUsage();
      memoryPool = ManagementFactory.getMemoryPoolMXBeans();
   }

   public MemoryUsage getMemoryUsage()
   {
      return memoryUsage;
   }

   public void setMemoryUsage(MemoryUsage memoryUsage)
   {
      this.memoryUsage = memoryUsage;
   }

   public MemoryUsage getNonMemoryUsage()
   {
      return nonMemoryUsage;
   }

   public void setNonMemoryUsage(MemoryUsage nonMemoryUsage)
   {
      this.nonMemoryUsage = nonMemoryUsage;
   }

   public List<MemoryPoolMXBean> getMemoryPool()
   {
      return memoryPool;
   }

   public void setMemoryPool(List<MemoryPoolMXBean> memoryPool)
   {
      this.memoryPool = memoryPool;
   }

   public void runGc()
   {
      logger.info("*************** start gc ***************");
      System.gc();
      logger.info("*************** end gc ***************");
   }

}
