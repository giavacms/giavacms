/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.controller.request;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.EmailUtils;
import org.giavacms.common.annotation.HttpRequestParam;
import org.giavacms.common.util.MessageUtils;
import org.giavacms.paypalweb.controller.session.ShoppingCartSessionController;
import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.giavacms.paypalweb.model.pojo.CountryCode;
import org.giavacms.paypalweb.util.CountryCodeUtils;
import org.jboss.logging.Logger;

@Named
@RequestScoped
public class PayerInfoRequestController
{
   @Inject
   @HttpRequestParam
   String billingFirstName;
   @Inject
   @HttpRequestParam
   String billingLastName;
   @Inject
   @HttpRequestParam
   String billingCompany;
   @Inject
   @HttpRequestParam
   String billingVatCode;
   @Inject
   @HttpRequestParam
   String billingLine1;
   @Inject
   @HttpRequestParam
   String billingLine2;
   @Inject
   @HttpRequestParam
   String billingCity;
   @Inject
   @HttpRequestParam
   String billingCountryCode;
   @Inject
   @HttpRequestParam
   String billingZip;
   @Inject
   @HttpRequestParam
   String billingState;
   @Inject
   @HttpRequestParam
   String billingPhone;
   @Inject
   @HttpRequestParam
   String billingEmail;
   @Inject
   @HttpRequestParam
   String notes;

   @Inject
   @HttpRequestParam
   String shippingFirstName;
   @Inject
   @HttpRequestParam
   String shippingLastName;
   @Inject
   @HttpRequestParam
   String shippingLine1;
   @Inject
   @HttpRequestParam
   String shippingLine2;
   @Inject
   @HttpRequestParam
   String shippingCity;
   @Inject
   @HttpRequestParam
   String shippingCountryCode;
   @Inject
   @HttpRequestParam
   String shippingZip;
   @Inject
   @HttpRequestParam
   String shippingState;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   PaypalConfigurationRequestController paypalConfigurationRequestController;

   @Inject
   ShoppingCartSessionController shoppingCartSessionController;

   public String addPayer()
   {
      StringBuffer errors = new StringBuffer();
      String sep = " ";
      if (billingFirstName != null && !billingFirstName.trim().isEmpty() &&
               billingLastName != null && !billingLastName.trim().isEmpty() &&
               billingCompany != null && !billingCompany.trim().isEmpty() &&
               // billingVatCode != null && !billingVatCode.trim().isEmpty() &&
               billingLine1 != null && !billingLine1.trim().isEmpty() &&
               billingCity != null && !billingCity.trim().isEmpty() &&
               billingCountryCode != null && !billingCountryCode.trim().isEmpty() &&
               billingZip != null && !billingZip.trim().isEmpty() &&
               billingState != null && !billingState.trim().isEmpty() &&
               billingPhone != null && !billingPhone.trim().isEmpty() &&
               billingEmail != null && !billingEmail.trim().isEmpty()
               && EmailUtils.isValidEmailAddress(billingEmail.trim()))
      {
         logger.info("billing address OK");
      }
      else
      {
         if (billingFirstName == null || billingFirstName.trim().isEmpty())
         {
            errors.append(sep + "manca il nome (fatturazione)");
            sep = " - ";
         }
         if (billingLastName == null || billingLastName.trim().isEmpty())
         {
            errors.append(sep + "manca il cognome (fatturazione)");
            sep = " - ";
         }
         if (billingCompany != null && billingCompany.trim().equals("true") && (billingVatCode == null
                  || billingVatCode.trim().isEmpty()))
         {
            errors.append(sep + "manca la partita iva (fatturazione)");
            sep = " - ";
         }
         if (billingLine1 == null || billingLine1.trim().isEmpty())
         {
            errors.append(sep + "manca l'indirizzo (fatturazione)");
            sep = " - ";
         }
         if (billingCity == null || billingCity.trim().isEmpty())
         {
            errors.append(sep + "manca la citta' (fatturazione)");
            sep = " - ";
         }
         if (billingCountryCode == null || billingCountryCode.trim().isEmpty())
         {
            errors.append(sep + "manca lo stato (fatturazione)");
            sep = " - ";
         }
         if (billingZip == null || billingZip.trim().isEmpty())
         {
            errors.append(sep + "manca il codice postale (fatturazione)");
            sep = " - ";
         }
         if (billingState == null || billingState.trim().isEmpty())
         {
            errors.append(sep + "manca la provincia (fatturazione)");
            sep = " - ";
         }
         if (billingPhone == null || billingPhone.trim().isEmpty())
         {
            errors.append(sep + "manca il telefono (fatturazione)");
            sep = " - ";
         }
         if (billingEmail == null || billingEmail.trim().isEmpty())
         {
            errors.append(sep + "manca la mail (fatturazione)");
            sep = " - ";
         }
         else if (!EmailUtils.isValidEmailAddress(billingEmail))
         {
            errors.append(sep + "la mail inserita non e' valida (fatturazione)");
            sep = " - ";
         }

      }
      if (notes != null && !notes.trim().isEmpty())
      {
         shoppingCartSessionController.setNotes(notes);
      }
      BillingAddress billingAddress = new BillingAddress(billingFirstName, billingLastName, billingCompany,
               billingVatCode, billingLine1, billingLine2,
               billingCity, billingCountryCode, billingZip, billingState, billingPhone, billingEmail);
      shoppingCartSessionController.addBillingAddress(billingAddress);
      if (!shoppingCartSessionController.isOneAddress())
      {
         if (shippingFirstName != null && !shippingFirstName.trim().isEmpty() &&
                  shippingLastName != null && !shippingLastName.trim().isEmpty() &&
                  shippingLine1 != null && !shippingLine1.trim().isEmpty() &&
                  shippingCity != null && !shippingCity.trim().isEmpty() &&
                  shippingCountryCode != null && !shippingCountryCode.trim().isEmpty() &&
                  shippingZip != null && !shippingZip.trim().isEmpty() &&
                  shippingState != null && !shippingState.trim().isEmpty())
         {
            logger.info("shipping address OK");
         }
         else
         {
            if (shippingFirstName == null || shippingFirstName.trim().isEmpty())
            {
               errors.append(sep + "manca il nome (consegna)");
               sep = " - ";
            }
            if (shippingLastName == null || shippingLastName.trim().isEmpty())
            {
               errors.append("; manca il cognome (consegna)");
               sep = " - ";
            }
            if (shippingLine1 == null || shippingLine1.trim().isEmpty())
            {
               errors.append("manca la l'indirizzo (consegna)");
               sep = " - ";
            }
            // if (shippingLine2 == null || shippingLine2.trim().isEmpty())
            // {
            // errors.append("; manca la linea2 (consegna)");
            // }
            if (shippingCity == null || shippingCity.trim().isEmpty())
            {
               errors.append(sep + "manca la citta' (consegna)");
               sep = " - ";
            }
            if (shippingCountryCode == null || shippingCountryCode.trim().isEmpty())
            {
               errors.append(sep + "manca lo stato (consegna)");
               sep = " - ";
            }
            if (shippingZip == null || shippingZip.trim().isEmpty())
            {
               errors.append(sep + "manca il codice postale (consegna)");
               sep = " - ";
            }
            if (shippingState == null || shippingState.trim().isEmpty())
            {
               errors.append(sep + "manca la provincia (consegna)");
               sep = " - ";
            }
         }
         ShippingAddress shippingAddress = new ShippingAddress(shippingFirstName, shippingLastName, shippingLine1,
                  shippingLine2, shippingCity, shippingCountryCode, shippingZip, shippingState);
         shoppingCartSessionController.addShippingAddress(shippingAddress);
      }
      else
      {
         ShippingAddress shippingAddress = new ShippingAddress(billingFirstName, billingLastName, billingLine1,
                  billingLine2, billingCity, billingCountryCode, billingZip, billingState);
         shoppingCartSessionController.addShippingAddress(shippingAddress);

      }
      if (errors.length() > 0)
      {
         logger.info(errors.toString());
         MessageUtils.addFacesMessage(errors.toString().substring(1), errors.toString().substring(1));
      }
      else
      {
         shoppingCartSessionController.save();
         try
         {
            FacesContext
                     .getCurrentInstance()
                     .getExternalContext()
                     .redirect(
                              paypalConfigurationRequestController.getPaypalConfiguration().getPreviewShoppingCartUrl());
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }

      }
      return null;
   }

   public String getBillingFirstName()
   {
      return billingFirstName;
   }

   public String getBillingLastName()
   {
      return billingLastName;
   }

   public String getBillingCompany()
   {
      if (billingCompany == null || billingCompany.trim().isEmpty())
         billingCompany = "false";
      return billingCompany;
   }

   public String getBillingVatCode()
   {
      return billingVatCode;
   }

   public String getBillingLine1()
   {
      return billingLine1;
   }

   public String getBillingLine2()
   {
      return billingLine2;
   }

   public String getBillingCity()
   {
      return billingCity;
   }

   public String getBillingCountryCode()
   {
      return billingCountryCode;
   }

   public String getBillingZip()
   {
      return billingZip;
   }

   public String getBillingState()
   {
      return billingState;
   }

   public String getBillingPhone()
   {
      return billingPhone;
   }

   public String getBillingEmail()
   {
      return billingEmail;
   }

   public String getShippingFirstName()
   {
      return shippingFirstName;
   }

   public String getShippingLastName()
   {
      return shippingLastName;
   }

   public String getShippingLine1()
   {
      return shippingLine1;
   }

   public String getShippingLine2()
   {
      return shippingLine2;
   }

   public String getShippingCity()
   {
      return shippingCity;
   }

   public String getShippingCountryCode()
   {
      return shippingCountryCode;
   }

   public String getShippingZip()
   {
      return shippingZip;
   }

   public String getShippingState()
   {
      return shippingState;
   }

   public Logger getLogger()
   {
      return logger;
   }

   public PaypalConfigurationRequestController getPaypalConfigurationRequestController()
   {
      return paypalConfigurationRequestController;
   }

   public ShoppingCartSessionController getShoppingCartSessionController()
   {
      return shoppingCartSessionController;
   }

   public List<CountryCode> getCountryCodes()
   {
      return CountryCodeUtils.getCountryCodes();
   }
}
