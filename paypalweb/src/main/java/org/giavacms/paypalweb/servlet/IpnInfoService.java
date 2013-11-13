/**
 * Paypal Button and Instant Payment Notification (IPN) Integration with Java
 * http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html
 */
package org.giavacms.paypalweb.servlet;

/**
 * Arbitrary service class to simulate the storage and retrieval of Paypal IPN Notification related information
 * 
 * User: smhumayun Date: 7/6/13 Time: 6:23 PM
 */
public class IpnInfoService
{

   /**
    * Store Paypal IPN Notification related information for future use
    * 
    * @param ipnInfo {@link IpnInfo}
    * @throws IpnException
    */
   public void log(final IpnInfo ipnInfo) throws IpnException
   {
      /**
       * Implementation...
       */
   }

   /**
    * Fetch Paypal IPN Notification related information saved earlier
    * 
    * @param txnId Paypal IPN Notification's Transaction ID
    * @return {@link IpnInfo}
    * @throws IpnException
    */
   public IpnInfo getIpnInfo(final String txnId) throws IpnException
   {
      IpnInfo ipnInfo = null;
      /**
       * Implementation...
       */
      return ipnInfo;
   }

}
