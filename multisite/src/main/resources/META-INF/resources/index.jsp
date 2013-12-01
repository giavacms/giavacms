
<%@page import="org.jboss.logging.Logger"%>
<%@page import="java.util.Properties"%>
<%
   Logger logger = Logger.getLogger("org.giavacms.multisite.jsp.index");
try {
   
}
   Properties properties = new Properties();
   String hostName = request.getServerName();
   properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("domains.properties"));
   for (String key : properties.stringPropertyNames())
   {
      String value = properties.getProperty(key);
      logger.info("." + key + "." + " => " + value);
   }
   logger.info("hostName: ." + hostName + ".");
   if (properties.containsKey(hostName))
   {
      String where = properties.getProperty(hostName);
      response.sendRedirect(request.getContextPath()
               + where);
   }
   else
   {
      response.sendRedirect(request.getContextPath()
               + "/p/index");
   }
%>
