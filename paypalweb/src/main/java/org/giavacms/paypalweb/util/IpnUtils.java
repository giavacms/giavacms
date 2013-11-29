package org.giavacms.paypalweb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.paypalweb.model.IpnContent;

public class IpnUtils
{

   static Logger logger = Logger.getLogger(IpnUtils.class.getName());

   public static IpnContent fromRequest(HttpServletRequest request, String res)
   {
      logger.info("inside IpnUtils.fromRequest");
      IpnContent ipnInfo = new IpnContent();
      ipnInfo.setDate(new Date());
      ipnInfo.setCustom(request.getParameter("custom"));
      // ipnInfo.setItemName(request.getParameter("item_name"));
      // ipnInfo.setItemNumber(request.getParameter("item_number"));
      ipnInfo.setPaymentStatus(request.getParameter("payment_status"));
      ipnInfo.setPaymentAmount(request.getParameter("mc_gross"));
      ipnInfo.setPaymentCurrency(request.getParameter("mc_currency"));
      ipnInfo.setTxnId(request.getParameter("txn_id"));
      ipnInfo.setReceiverEmail(request.getParameter("receiver_email"));
      ipnInfo.setPayerEmail(request.getParameter("payer_email"));
      ipnInfo.setResponse(res);
      ipnInfo.setRequestParams(getAllRequestParams(request));
      return ipnInfo;
   }

   public static String postToPaypal(HttpServletRequest request, String serviceUrl) throws IOException
   {
      logger.info("inside IpnUtils.postToPaypal: " + serviceUrl);
      // 1. Prepare 'notify-validate' command with exactly the same parameters
      Enumeration<String> en = request.getParameterNames();
      StringBuilder cmd = new StringBuilder("cmd=_notify-validate");
      String paramName;
      String paramValue;
      while (en.hasMoreElements())
      {

         paramName = (String) en.nextElement();
         paramValue = request.getParameter(paramName);
         logger.info(paramName + ": " + paramValue);
         cmd.append("&").append(paramName).append("=")
                  .append(URLEncoder.encode(paramValue, "UTF-8"));
      }

      // 2. Post above command to Paypal Service URL
      /*
       * For Production/Live - https://www.paypal.com/cgi-bin/webscr
       * 
       * For Sandbox/Testing - https://www.sandbox.paypal.com/cgi-bin/webscr
       */
      URL u = new URL(serviceUrl);
      HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
      uc.setDoOutput(true);
      uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      uc.setRequestProperty("Host", "www.paypal.com");
      PrintWriter pw = new PrintWriter(uc.getOutputStream());
      pw.println(cmd.toString());
      pw.close();

      // 3. Read response from Paypal
      BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      String res = in.readLine();
      in.close();
      return res;
   }

   public static String getAllRequestParams(HttpServletRequest request)
   {
      StringBuilder sb = new StringBuilder("\nREQUEST PARAMETERS\n");
      for (String key : request.getParameterMap().keySet())
      {
         String[] value = request.getParameterMap().get(key);
         sb.append(key).append(": \n").append(Arrays.toString(value)).append("\n");
      }
      return sb.toString();
   }
}
