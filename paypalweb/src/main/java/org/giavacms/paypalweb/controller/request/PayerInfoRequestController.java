package org.giavacms.paypalweb.controller.request;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.util.MessageUtils;
import org.giavacms.paypalweb.controller.session.ShoppingCartSessionController;
import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.jboss.logging.Logger;

@Named
@RequestScoped
public class PayerInfoRequestController
{
   @Inject
   @HttpParam
   String billingFirstName;
   @Inject
   @HttpParam
   String billingLastName;
   @Inject
   @HttpParam
   String billingCompany;
   @Inject
   @HttpParam
   String billingVatCode;
   @Inject
   @HttpParam
   String billingLine1;
   @Inject
   @HttpParam
   String billingLine2;
   @Inject
   @HttpParam
   String billingCity;
   @Inject
   @HttpParam
   String billingCountryCode;
   @Inject
   @HttpParam
   String billingZip;
   @Inject
   @HttpParam
   String billingState;
   @Inject
   @HttpParam
   String billingPhone;
   @Inject
   @HttpParam
   String billingEmail;

   @Inject
   @HttpParam
   String shippingFirstName;
   @Inject
   @HttpParam
   String shippingLastName;
   @Inject
   @HttpParam
   String shippingLine1;
   @Inject
   @HttpParam
   String shippingLine2;
   @Inject
   @HttpParam
   String shippingCity;
   @Inject
   @HttpParam
   String shippingCountryCode;
   @Inject
   @HttpParam
   String shippingZip;
   @Inject
   @HttpParam
   String shippingState;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   PaypalConfigurationRequestController paypalConfigurationRequestController;

   @Inject
   ShoppingCartSessionController shoppingCartSessionController;

   public String addPayer()
   {
      StringBuffer errors = new StringBuffer();
      if (billingFirstName != null && !billingFirstName.trim().isEmpty() &&
               billingLastName != null && !billingLastName.trim().isEmpty() &&
               billingCompany != null && !billingCompany.trim().isEmpty() &&
               billingVatCode != null && !billingVatCode.trim().isEmpty() &&
               billingLine1 != null && !billingLine1.trim().isEmpty() &&
               billingCity != null && !billingCity.trim().isEmpty() &&
               billingCountryCode != null && !billingCountryCode.trim().isEmpty() &&
               billingZip != null && !billingZip.trim().isEmpty() &&
               billingState != null && !billingState.trim().isEmpty() &&
               billingPhone != null && !billingPhone.trim().isEmpty() &&
               billingEmail != null && !billingEmail.trim().isEmpty())
      {
         BillingAddress billingAddress = new BillingAddress(billingFirstName, billingLastName, billingCompany,
                  billingVatCode, billingLine1, billingLine2,
                  billingCity, billingCountryCode, billingZip, billingState, billingPhone, billingEmail);
         shoppingCartSessionController.addBillingAddress(billingAddress);
      }
      else
      {
         if (billingFirstName != null && !billingFirstName.trim().isEmpty())
         {
            errors.append("; manca il nome (fatturazione)");
         }
         if (billingLastName != null && !billingLastName.trim().isEmpty())
         {
            errors.append("; manca il cognome (fatturazione)");
         }

         if (billingVatCode != null && !billingVatCode.trim().isEmpty())
         {
            errors.append("; manca la partita iva/codice fiscale (fatturazione)");
         }
         if (billingLine1 != null && !billingLine1.trim().isEmpty())
         {
            errors.append("; manca la linea1 (fatturazione)");
         }
         if (billingLine2 != null && !billingLine2.trim().isEmpty())
         {
            errors.append("; manca la linea2 (fatturazione)");
         }
         if (billingCity != null && !billingCity.trim().isEmpty())
         {
            errors.append("; manca la citta' (fatturazione)");
         }
         if (billingCountryCode != null && !billingCountryCode.trim().isEmpty())
         {
            errors.append("; manca la provincia (fatturazione)");
         }
         if (billingZip != null && !billingZip.trim().isEmpty())
         {
            errors.append("manca il codice postale (fatturazione)");
         }
         if (billingState != null && !billingState.trim().isEmpty())
         {
            errors.append("; manca lo stato (fatturazione)");
         }
         if (billingPhone != null && !billingPhone.trim().isEmpty())
         {
            errors.append("; manca il telefono (fatturazione)");
         }
         if (billingEmail != null && !billingEmail.trim().isEmpty())
         {
            errors.append("; manca la mail (fatturazione)");
         }

      }
      if (shippingFirstName != null && !shippingFirstName.trim().isEmpty() &&
               shippingLastName != null && !shippingLastName.trim().isEmpty() &&
               shippingLine1 != null && !shippingLine1.trim().isEmpty() &&
               shippingCity != null && !shippingCity.trim().isEmpty() &&
               shippingCountryCode != null && !shippingCountryCode.trim().isEmpty() &&
               shippingZip != null && !shippingZip.trim().isEmpty() &&
               shippingState != null && !shippingState.trim().isEmpty())
      {

         ShippingAddress shippingAddress = new ShippingAddress(shippingFirstName, shippingLastName, shippingLine1,
                  shippingLine2, shippingCity, shippingCountryCode, shippingZip, shippingState);
         shoppingCartSessionController.addShippingAddress(shippingAddress);
      }
      else
      {
         if (shippingFirstName != null && !shippingFirstName.trim().isEmpty())
         {
            errors.append("; manca il nome (consegna)");
         }
         if (shippingLastName != null && !shippingLastName.trim().isEmpty())
         {
            errors.append("; manca il cognome (consegna)");
         }
         if (shippingLine1 != null && !shippingLine1.trim().isEmpty())
         {
            errors.append("; manca la linea1 (consegna)");
         }
         if (shippingLine2 != null && !shippingLine2.trim().isEmpty())
         {
            errors.append("; manca la linea2 (consegna)");
         }
         if (shippingCity != null && !shippingCity.trim().isEmpty())
         {
            errors.append("; manca la citta' (consegna)");
         }
         if (shippingCountryCode != null && !shippingCountryCode.trim().isEmpty())
         {
            errors.append("; manca la provincia (consegna)");
         }
         if (shippingZip != null && !shippingZip.trim().isEmpty())
         {
            errors.append("; manca il codice postale (consegna)");
         }
         if (shippingState != null && !shippingState.trim().isEmpty())
         {
            errors.append("; manca lo stato (consegna)");
         }
      }

      if (errors.length() > 0)
      {
         logger.info(errors.toString());
         MessageUtils.addFacesMessage("", errors.toString().substring(1));
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
}
