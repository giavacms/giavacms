package org.giavacms.paypalweb.model.enums;

public enum PaypalStatus
{
   // The status of the payment:
   // Canceled_Reversal: A reversal has been canceled. For example, you won a dispute with the customer, and the funds
   // for the transaction that was reversed have been returned to you.
   // Completed: The payment has been completed, and the funds have been added successfully to your account balance.
   // Created: A German ELV payment is made using Express Checkout.
   // Denied: The payment was denied. This happens only if the payment was previously pending because of one of the
   // reasons listed for the pending_reason variable or the Fraud_Management_Filters_x variable.
   // Expired: This authorization has expired and cannot be captured.
   // Failed: The payment has failed. This happens only if the payment was made from your customer's bank account.
   // Pending: The payment is pending. See pending_reason for more information.
   // Refunded: You refunded the payment.
   // Reversed: A payment was reversed due to a chargeback or other type of reversal. The funds have been removed from
   // your account balance and returned to the buyer. The reason for the reversal is specified in the ReasonCode
   // element.
   // Processed: A payment has been accepted.
   // Voided: This authorization has been voided.
   Canceled_Reversal, Created, Denied, Expired, Failed, Reversed, Processed, Voided,
   // PENDING: TEST
   Pending,
   // REFUND
   Refunded,
   // PAYED
   Completed,
   // INTERNAL STATE AFTER PERSIST THE SHOPPING CART
   Init,
   // UNDO
   Undo,
   // NOT COMPLETED
   NotCompleted,
   // SENT
   Sent;

   public static PaypalStatus get(String status)
   {
      if (status != null && !status.trim().isEmpty())
      {
         for (PaypalStatus paypalStatus : PaypalStatus.values())
         {
            if (paypalStatus.name().equals(status.trim().toLowerCase()))
               return paypalStatus;
         }
      }
      return null;
   }
}
