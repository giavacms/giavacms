/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.audit.interceptor;

import org.giavacms.audit.annotation.AuditEnabled;
import org.giavacms.audit.annotation.AuditDisabled;
import org.jboss.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@AuditEnabled
@Interceptor
public class AuditInterceptor
{
   @Inject
   Instance<AuditWriter> logWriters;

   Logger logger = Logger.getLogger(AuditInterceptor.class);

   @AroundInvoke
   public Object manageTransaction(InvocationContext ctx) throws Exception
   {
      String className = ctx.getTarget().getClass().getName();
      String methodName = ctx.getMethod().getName();

      String params = Arrays.toString(ctx.getParameters());
      Object result = ctx.proceed();
      if (ctx.getMethod().getAnnotation(AuditDisabled.class) != null)
      {
         logger.info("NOLOG: className: " + className + " methodname: "
                  + methodName);
      }
      else if (logWriters != null)
      {
         for (AuditWriter auditWriter : logWriters)
         {
            auditWriter.write(className, methodName, params, result);
         }
      }
      else
         logger.info("className: " + className + " methodname: "
                  + methodName + " params:" + params + " result: "
                  + result.toString());
      return result;
   }
}
