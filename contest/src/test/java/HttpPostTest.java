/**
 * Created by fiorenzo on 06/07/15.
 */
/*
    If you use maven, add the folowing dependency to your pom.xml.
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.1.1</version>
    </dependency>

    Otherwise download Apache HttpComponents from http://hc.apache.org/
    and add the libs to your classpath.
*/

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

public class HttpPostTest
{

   public static void main(String[] args) throws Exception
   {
      //      Single dispatch
      //      String [] recipients = new String[]{"391234567890"};
      //      Multiple dispatch
      String[] recipients = new String[] { "393922274929" };

      String username = "fiorenzino";
      String password = "g3sucr1st0";

      // SMS CLASSIC dispatch with custom alphanumeric sender
      String result = skebbyGatewaySendSMS(username, password, recipients, "vota la tua estate", "send_sms_basic",
               null, "votaestate");

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
      System.out.println("result: " + result);
   }

   protected static String skebbyGatewaySendSMS(String username, String password, String[] recipients, String text,
            String smsType, String senderNumber, String senderString) throws IOException
   {
      return skebbyGatewaySendSMS(username, password, recipients, text, smsType, senderNumber, senderString, "UTF-8");
   }

   protected static String skebbyGatewaySendSMS(String username, String password, String[] recipients, String text,
            String smsType, String senderNumber, String senderString, String charset) throws IOException
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
      formparams.add(new BasicNameValuePair("method", smsType));
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
