package org.giavacms.instagram.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.giavacms.common.util.JSFUtils;
import org.giavacms.instagram.controller.InstagramConfigurationController;


@WebServlet(urlPatterns = "/instagram/")
public class InstagramServlet extends HttpServlet {

	@Inject
	InstagramConfigurationController instagramConfigurationController;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGetOrPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGetOrPost(req, resp);
	}

	private void doGetOrPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = "code";
		String value = req.getParameter(name);
		String error_type = "error_type";
		if (value == null) {
			instagramConfigurationController
					.setLogOperation("OK CODE AGGANCIATO");
			instagramConfigurationController.getElement().setCode(value);
		} else if ("".equals(value)) {
			instagramConfigurationController.setLogOperation("NO CODE"
					+ req.getParameter(error_type));
		}
		JSFUtils.redirect("/private/instagram/configuration.jsf");
	}

}
