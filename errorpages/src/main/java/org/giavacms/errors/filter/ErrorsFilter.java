package org.giavacms.errors.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.giavacms.common.util.BeanUtils;
import org.giavacms.common.util.FileUtils;
import org.giavacms.errors.model.Errors;
import org.giavacms.errors.producer.ErrorsProducer;

import com.ocpsoft.pretty.faces.servlet.PrettyFacesWrappedResponse;

/**
 * Still does not work
 * 
 * @author pisi79
 * 
 */
public class ErrorsFilter implements Filter {

	List<org.giavacms.errors.model.Errors> errors = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		errors = BeanUtils.getBean(ErrorsProducer.class).getErrors();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// Create a wrappedResponse object done before chain.doFilter as this is
		// what is passed to the servlet
		HttpServletResponse httpResp = (HttpServletResponse) response;

		// Called by the container each time a request/response pair is passed
		// through the chain due to a client request.
		try {
			chain.doFilter(request, httpResp);
			int status = 200;
			// use getStatus to check for 404 after the fact
			if (httpResp instanceof PrettyFacesWrappedResponse) {
				status = ((HttpServletResponse) ((PrettyFacesWrappedResponse) httpResp)
						.getResponse()).getStatus();
			} else {
				status = httpResp.getStatus();
			}
			if (status != 200) {
				for (Errors error : errors) {
					if (error.getHttpCode() == httpResp.getStatus()) {
						// ILLEGAL STATE. RESPONSE IS COMMITTED AND NO REDIRECT
						// CAN BE APPLIED TO
						// httpResp.sendRedirect(error.getPage());
						// ILLEGAL STATE. RESPONSE IS COMMITTED AND NO FORWARD
						// CAN BE APPLIED TO
						// request.getRequestDispatcher("/" +
						// error.getPage()).forward(request, response);
						FileUtils.writeBytesToOutputStream(
								response.getOutputStream(),
								"<html><body>Non trovato!!!</body></html>"
										.getBytes());
						break;
					}
				}
			}

		} catch (Throwable t) {
			// ILLEGAL STATE. RESPONSE IS COMMITTED AND NO REDIRECT CAN BE
			// APPLIED TO
			for (Errors error : errors) {
				try {
					@SuppressWarnings("rawtypes")
					Class throwable = Class.forName(error.getExceptionClass());
					if (t.getClass().isAssignableFrom(throwable)) {
						// ILLEGAL STATE. RESPONSE IS COMMITTED AND NO REDIRECT
						// CAN BE APPLIED TO
						httpResp.sendRedirect(error.getPage());
						// ILLEGAL STATE. RESPONSE IS COMMITTED AND NO FORWARD
						// CAN BE APPLIED TO
						request.getRequestDispatcher("/" + error.getPage())
								.forward(request, response);
					}
					break;
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

}
