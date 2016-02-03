<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	try {
		if (session != null) {
			session.invalidate();
		}
		String redirectURL = "/private/index.jsp";
		response.sendRedirect(redirectURL);

	} catch (Exception e) {
		e.printStackTrace();
		String redirectURL = "/private/index.jsp";
		response.sendRedirect(redirectURL);
	}
%>
