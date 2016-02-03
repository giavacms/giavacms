<%
    try
    {
        //it.coopservice.medsos.util.HttpRequestUtils.print(request);
        HttpSession sess = request.getSession();
        if (session != null)
        {
            // NO!
//         session.invalidate();
        }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    response.sendRedirect(request.getContextPath()
            + "/login.jsp?message=Username+o+password+errati");
%>
