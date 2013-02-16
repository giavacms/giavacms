package by.giava.giavacms.errors.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import by.giava.common.util.BeanUtils;
import by.giava.giavacms.errors.model.Errors;
import by.giava.giavacms.errors.producer.ErrorsProducer;

public class ErrorsFilter implements Filter {

	List<by.giava.giavacms.errors.model.Errors> errors = null;

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
			// use getStatus to check for 404 after the fact
			if (httpResp.getStatus() != 200) {
				for (Errors error : errors) {
					if (error.getHttpCode() == httpResp.getStatus()) {
						try {
							httpResp.sendRedirect(error.getPage());
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
		} catch (Throwable t) {
			for (Errors error : errors) {
				try {
					@SuppressWarnings("rawtypes")
					Class throwable = Class.forName(error.getExceptionClass());
					if (t.getClass().isAssignableFrom(throwable)) {
						httpResp.sendRedirect(error.getPage());
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	@Override
	public void destroy() {
	}

}
