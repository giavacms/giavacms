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

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.paypalweb.model.IpnContent;

public class IpnUtils
{
   public static IpnContent fromRequest(HttpServletRequest request, String res)
   {
      IpnContent ipnInfo = new IpnContent();
      ipnInfo.setDate(new Date());
      ipnInfo.setCustom(request.getParameter("custom"));
//      ipnInfo.setItemName(request.getParameter("item_name"));
      //      ipnInfo.setItemNumber(request.getParameter("item_number"));
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

   public static String postToPaypal(HttpServletRequest request, String ipnUrl) throws IOException
   {

      // 1. Prepare 'notify-validate' command with exactly the same parameters
      Enumeration<String> en = request.getParameterNames();
      StringBuilder cmd = new StringBuilder("cmd=_notify-validate");
      String paramName;
      String paramValue;
      while (en.hasMoreElements())
      {
         paramName = (String) en.nextElement();
         paramValue = request.getParameter(paramName);
         cmd.append("&").append(paramName).append("=")
                  .append(URLEncoder.encode(paramValue, request.getParameter("charset")));
      }

      // 2. Post above command to Paypal IPN URL
      URL u = new URL(ipnUrl);
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
