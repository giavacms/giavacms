/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UTFEncodingFilter implements Filter
{

   public void init(FilterConfig config) throws ServletException
   {

   }

   @Override
   public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
   {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      chain.doFilter(request, response);
   }

   public void destroy()
   {
   }
}
