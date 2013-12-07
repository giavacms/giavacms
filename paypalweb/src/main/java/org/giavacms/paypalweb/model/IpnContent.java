/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = IpnContent.TABLE_NAME)
public class IpnContent implements Serializable
{

   private static final long serialVersionUID = 1L;
   /*
    * $payment_status = $_POST['payment_status']; $total = $_POST['mc_gross']; $txn_id = $_POST['txn_id'];
    * $payment_currency = $_POST['mc_currency']; $cartid = $_POST['custom']; $my_email = $_POST['business']; $email =
    * $_POST['payer_email'];
    */
   public static final String TABLE_NAME = "PPW_IpnContent";
   private Long id;
   private String paymentStatus;
   private String paymentAmount;
   private String txnId;
   private String paymentCurrency;
   private String custom;
   private String receiverEmail;
   private String payerEmail;
   private String response;
   private String requestParams;
   private Date date;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getPayerEmail()
   {
      return payerEmail;
   }

   public void setPayerEmail(String payerEmail)
   {
      this.payerEmail = payerEmail;
   }

   public String getPaymentAmount()
   {
      return paymentAmount;
   }

   public void setPaymentAmount(String paymentAmount)
   {
      this.paymentAmount = paymentAmount;
   }

   public String getPaymentCurrency()
   {
      return paymentCurrency;
   }

   public void setPaymentCurrency(String paymentCurrency)
   {
      this.paymentCurrency = paymentCurrency;
   }

   public String getPaymentStatus()
   {
      return paymentStatus;
   }

   public void setPaymentStatus(String paymentStatus)
   {
      this.paymentStatus = paymentStatus;
   }

   public String getReceiverEmail()
   {
      return receiverEmail;
   }

   public void setReceiverEmail(String receiverEmail)
   {
      this.receiverEmail = receiverEmail;
   }

   @Lob
   public String getRequestParams()
   {
      return requestParams;
   }

   public void setRequestParams(String requestParams)
   {
      this.requestParams = requestParams;
   }

   public String getResponse()
   {
      return response;
   }

   public void setResponse(String response)
   {
      this.response = response;
   }

   public String getTxnId()
   {
      return txnId;
   }

   public void setTxnId(String txnId)
   {
      this.txnId = txnId;
   }

   public String getCustom()
   {
      return custom;
   }

   public void setCustom(String custom)
   {
      this.custom = custom;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   @Override
   public String toString()
   {
      return "IpnContent [paymentStatus=" + paymentStatus
               + ", paymentAmount=" + paymentAmount + ", paymentCurrency=" + paymentCurrency + ", txnId=" + txnId
               + ", receiverEmail=" + receiverEmail + ", payerEmail=" + payerEmail + ", response=" + response
               + ", requestParams=" + requestParams + ", date=" + date + ", custom=" + custom
               + "]";
   }

}