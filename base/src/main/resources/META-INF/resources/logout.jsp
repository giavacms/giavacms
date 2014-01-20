
<%
   try
   {
      HttpSession sess = request.getSession();
      if (session != null)
      {
         session.invalidate();
      }
   }
   catch (Exception e)
   {
      e.printStackTrace();
   }
   response.sendRedirect(request.getContextPath()
            + "/index.jsp");
%>
