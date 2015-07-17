package org.giavacms.contest.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiorenzo on 07/07/15.
 */
public class SkebbySmsUtils
{

   // SMS Basic dispatch
   // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_basic", null, null);

   // SMS CLASSIC dispatch with custom numeric sender
   // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic", "393471234567", null);

   // SMS CLASSIC PLUS dispatch (with delivery report) with custom alphanumeric sender
   // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", null, "John");

   // SMS CLASSIC PLUS dispatch (with delivery report) with custom numeric sender
   // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", "393471234567", null);

   // ------------------------------------------------------------------
   // Check the complete documentation at http://www.skebby.com/business/index/send-docs/
   // ------------------------------------------------------------------
   // For eventual errors see http:#www.skebby.com/business/index/send-docs/#errorCodesSection
   // WARNING: in case of error DON'T retry the sending, since they are blocking errors
   // ------------------------------------------------------------------

   public static String skebbyGatewaySendSMS(String username, String password, String[] recipients, String text,
            SkebbyType smsType, String senderNumber, String senderString, String charset) throws IOException
   {

      if (!charset.equals("UTF-8") && !charset.equals("ISO-8859-1"))
      {

         throw new IllegalArgumentException("Charset not supported.");
      }

      String endpoint = "http://gateway.skebby.it/api/send/smseasy/advanced/http.php";
      HttpParams params = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
      DefaultHttpClient httpclient = new DefaultHttpClient(params);
      HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
      paramsBean.setVersion(HttpVersion.HTTP_1_1);
      paramsBean.setContentCharset(charset);
      paramsBean.setHttpElementCharset(charset);

      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      formparams.add(new BasicNameValuePair("method", smsType.name()));
      formparams.add(new BasicNameValuePair("username", username));
      formparams.add(new BasicNameValuePair("password", password));
      if (null != senderNumber)
         formparams.add(new BasicNameValuePair("sender_number", senderNumber));
      if (null != senderString)
         formparams.add(new BasicNameValuePair("sender_string", senderString));

      for (String recipient : recipients)
      {
         formparams.add(new BasicNameValuePair("recipients[]", recipient));
      }
      formparams.add(new BasicNameValuePair("text", text));
      formparams.add(new BasicNameValuePair("charset", charset));

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
      HttpPost post = new HttpPost(endpoint);
      post.setEntity(entity);

      HttpResponse response = httpclient.execute(post);
      HttpEntity resultEntity = response.getEntity();
      if (null != resultEntity)
      {
         return EntityUtils.toString(resultEntity);
      }
      return null;
   }
}
