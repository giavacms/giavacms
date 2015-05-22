/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.audit.interceptor;

import org.giavacms.audit.annotation.LogOperation;
import org.giavacms.audit.annotation.NoLogOperation;
import org.jboss.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@LogOperation
@Interceptor
public class LogOperationInterceptor
{
   @Inject
   Instance<LogWriter> logWriters;

   Logger logger = Logger.getLogger(LogOperationInterceptor.class);

   @AroundInvoke
   public Object manageTransaction(InvocationContext ctx) throws Exception
   {
      String className = ctx.getTarget().getClass().getName();
      String methodName = ctx.getMethod().getName();

      String params = Arrays.toString(ctx.getParameters());
      Object result = ctx.proceed();
      if (ctx.getMethod().getAnnotation(NoLogOperation.class) != null)
      {
         logger.info("NOLOG: className: " + className + " methodname: "
                  + methodName);
      }
      else if (logWriters != null)
      {
         for (LogWriter logWriter : logWriters)
         {
            logWriter.write(className, methodName, params, result);
         }
      }
      else
         logger.info("className: " + className + " methodname: "
                  + methodName + " params:" + params + " result: "
                  + result.toString());
      return result;
   }
}
