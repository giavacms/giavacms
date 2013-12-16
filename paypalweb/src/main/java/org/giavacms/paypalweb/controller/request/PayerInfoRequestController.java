/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.controller.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.EmailUtils;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.util.MessageUtils;
import org.giavacms.paypalweb.controller.session.ShoppingCartSessionController;
import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.giavacms.paypalweb.model.pojo.CountryCode;
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
   String notes;

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
               billingEmail != null && !billingEmail.trim().isEmpty())
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
         if (billingCompany != null && billingCompany.equals("true") && (billingVatCode == null
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
      List<CountryCode> countries = new ArrayList<CountryCode>();
      countries.add(new CountryCode("AF", "AFGHANISTAN"));
      countries.add(new CountryCode("AX", "ALAND ISLANDS"));
      countries.add(new CountryCode("AL", "ALBANIA"));
      countries.add(new CountryCode("DZ", "ALGERIA"));
      countries.add(new CountryCode("AS", "AMERICAN SAMOA"));
      countries.add(new CountryCode("AD", "ANDORRA"));
      countries.add(new CountryCode("AO", "ANGOLA"));
      countries.add(new CountryCode("AI", "ANGUILLA"));
      countries.add(new CountryCode("AQ", "ANTARCTICA"));
      countries.add(new CountryCode("AG", "ANTIGUA AND BARBUDA"));
      countries.add(new CountryCode("AR", "ARGENTINA"));
      countries.add(new CountryCode("AM", "ARMENIA"));
      countries.add(new CountryCode("AW", "ARUBA"));
      countries.add(new CountryCode("AU", "AUSTRALIA"));
      countries.add(new CountryCode("AT", "AUSTRIA"));
      countries.add(new CountryCode("AZ", "AZERBAIJAN"));
      countries.add(new CountryCode("BS", "BAHAMAS"));
      countries.add(new CountryCode("BH", "BAHRAIN"));
      countries.add(new CountryCode("BD", "BANGLADESH"));
      countries.add(new CountryCode("BB", "BARBADOS"));
      countries.add(new CountryCode("BY", "BELARUS"));
      countries.add(new CountryCode("BE", "BELGIUM"));
      countries.add(new CountryCode("BZ", "BELIZE"));
      countries.add(new CountryCode("BJ", "BENIN"));
      countries.add(new CountryCode("BM", "BERMUDA"));
      countries.add(new CountryCode("BT", "BHUTAN"));
      countries.add(new CountryCode("BO", "BOLIVIA, PLURINATIONAL STATE OF"));
      countries.add(new CountryCode("BQ", "BONAIRE, SINT EUSTATIUS AND SABA"));
      countries.add(new CountryCode("BA", "BOSNIA AND HERZEGOVINA"));
      countries.add(new CountryCode("BW", "BOTSWANA"));
      countries.add(new CountryCode("BV", "BOUVET ISLAND"));
      countries.add(new CountryCode("BR", "BRAZIL"));
      countries.add(new CountryCode("IO", "BRITISH INDIAN OCEAN TERRITORY"));
      countries.add(new CountryCode("BN", "BRUNEI DARUSSALAM"));
      countries.add(new CountryCode("BG", "BULGARIA"));
      countries.add(new CountryCode("BF", "BURKINA FASO"));
      countries.add(new CountryCode("BI", "BURUNDI"));
      countries.add(new CountryCode("KH", "CAMBODIA"));
      countries.add(new CountryCode("CM", "CAMEROON"));
      countries.add(new CountryCode("CA", "CANADA"));
      countries.add(new CountryCode("CV", "CAPE VERDE"));
      countries.add(new CountryCode("KY", "CAYMAN ISLANDS"));
      countries.add(new CountryCode("CF", "CENTRAL AFRICAN REPUBLIC"));
      countries.add(new CountryCode("TD", "CHAD"));
      countries.add(new CountryCode("CL", "CHILE"));
      countries.add(new CountryCode("CN", "CHINA"));
      countries.add(new CountryCode("CX", "CHRISTMAS ISLAND"));
      countries.add(new CountryCode("CC", "COCOS (KEELING) ISLANDS"));
      countries.add(new CountryCode("CO", "COLOMBIA"));
      countries.add(new CountryCode("KM", "COMOROS"));
      countries.add(new CountryCode("CG", "CONGO"));
      countries.add(new CountryCode("CD", "CONGO, THE DEMOCRATIC REPUBLIC OF THE"));
      countries.add(new CountryCode("CK", "COOK ISLANDS"));
      countries.add(new CountryCode("CR", "COSTA RICA"));
      countries.add(new CountryCode("CI", "CÔTE D'IVOIRE"));
      countries.add(new CountryCode("HR", "CROATIA"));
      countries.add(new CountryCode("CU", "CUBA"));
      countries.add(new CountryCode("CW", "CURAÇAO"));
      countries.add(new CountryCode("CY", "CYPRUS"));
      countries.add(new CountryCode("CZ", "CZECH REPUBLIC"));
      countries.add(new CountryCode("DK", "DENMARK"));
      countries.add(new CountryCode("DJ", "DJIBOUTI"));
      countries.add(new CountryCode("DM", "DOMINICA"));
      countries.add(new CountryCode("DO", "DOMINICAN REPUBLIC"));
      countries.add(new CountryCode("EC", "ECUADOR"));
      countries.add(new CountryCode("EG", "EGYPT"));
      countries.add(new CountryCode("SV", "EL SALVADOR"));
      countries.add(new CountryCode("GQ", "EQUATORIAL GUINEA"));
      countries.add(new CountryCode("ER", "ERITREA"));
      countries.add(new CountryCode("EE", "ESTONIA"));
      countries.add(new CountryCode("ET", "ETHIOPIA"));
      countries.add(new CountryCode("FK", "FALKLAND ISLANDS (MALVINAS)"));
      countries.add(new CountryCode("FO", "FAROE ISLANDS"));
      countries.add(new CountryCode("FJ", "FIJI"));
      countries.add(new CountryCode("FI", "FINLAND"));
      countries.add(new CountryCode("FR", "FRANCE"));
      countries.add(new CountryCode("GF", "FRENCH GUIANA"));
      countries.add(new CountryCode("PF", "FRENCH POLYNESIA"));
      countries.add(new CountryCode("TF", "FRENCH SOUTHERN TERRITORIES"));
      countries.add(new CountryCode("GA", "GABON"));
      countries.add(new CountryCode("GM", "GAMBIA"));
      countries.add(new CountryCode("GE", "GEORGIA"));
      countries.add(new CountryCode("DE", "GERMANY"));
      countries.add(new CountryCode("GH", "GHANA"));
      countries.add(new CountryCode("GI", "GIBRALTAR"));
      countries.add(new CountryCode("GR", "GREECE"));
      countries.add(new CountryCode("GL", "GREENLAND"));
      countries.add(new CountryCode("GD", "GRENADA"));
      countries.add(new CountryCode("GP", "GUADELOUPE"));
      countries.add(new CountryCode("GU", "GUAM"));
      countries.add(new CountryCode("GT", "GUATEMALA"));
      countries.add(new CountryCode("GG", "GUERNSEY"));
      countries.add(new CountryCode("GN", "GUINEA"));
      countries.add(new CountryCode("GW", "GUINEA-BISSAU"));
      countries.add(new CountryCode("GY", "GUYANA"));
      countries.add(new CountryCode("HT", "HAITI"));
      countries.add(new CountryCode("HM", "HEARD ISLAND AND MCDONALD ISLANDS"));
      countries.add(new CountryCode("VA", "HOLY SEE (VATICAN CITY STATE)"));
      countries.add(new CountryCode("HN", "HONDURAS"));
      countries.add(new CountryCode("HK", "HONG KONG"));
      countries.add(new CountryCode("HU", "HUNGARY"));
      countries.add(new CountryCode("IS", "ICELAND"));
      countries.add(new CountryCode("IN", "INDIA"));
      countries.add(new CountryCode("ID", "INDONESIA"));
      countries.add(new CountryCode("IR", "IRAN, ISLAMIC REPUBLIC OF"));
      countries.add(new CountryCode("IQ", "IRAQ"));
      countries.add(new CountryCode("IE", "IRELAND"));
      countries.add(new CountryCode("IM", "ISLE OF MAN"));
      countries.add(new CountryCode("IL", "ISRAEL"));
      countries.add(new CountryCode("IT", "ITALY"));
      countries.add(new CountryCode("JM", "JAMAICA"));
      countries.add(new CountryCode("JP", "JAPAN"));
      countries.add(new CountryCode("JE", "JERSEY"));
      countries.add(new CountryCode("JO", "JORDAN"));
      countries.add(new CountryCode("KZ", "KAZAKHSTAN"));
      countries.add(new CountryCode("KE", "KENYA"));
      countries.add(new CountryCode("KI", "KIRIBATI"));
      countries.add(new CountryCode("KP", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF"));
      countries.add(new CountryCode("KR", "KOREA, REPUBLIC OF"));
      countries.add(new CountryCode("KW", "KUWAIT"));
      countries.add(new CountryCode("KG", "KYRGYZSTAN"));
      countries.add(new CountryCode("LA", "LAO PEOPLE'S DEMOCRATIC REPUBLIC"));
      countries.add(new CountryCode("LV", "LATVIA"));
      countries.add(new CountryCode("LB", "LEBANON"));
      countries.add(new CountryCode("LS", "LESOTHO"));
      countries.add(new CountryCode("LR", "LIBERIA"));
      countries.add(new CountryCode("LY", "LIBYA"));
      countries.add(new CountryCode("LI", "LIECHTENSTEIN"));
      countries.add(new CountryCode("LT", "LITHUANIA"));
      countries.add(new CountryCode("LU", "LUXEMBOURG"));
      countries.add(new CountryCode("MO", "MACAO"));
      countries.add(new CountryCode("MK", "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF"));
      countries.add(new CountryCode("MG", "MADAGASCAR"));
      countries.add(new CountryCode("MW", "MALAWI"));
      countries.add(new CountryCode("MY", "MALAYSIA"));
      countries.add(new CountryCode("MV", "MALDIVES"));
      countries.add(new CountryCode("ML", "MALI"));
      countries.add(new CountryCode("MT", "MALTA"));
      countries.add(new CountryCode("MH", "MARSHALL ISLANDS"));
      countries.add(new CountryCode("MQ", "MARTINIQUE"));
      countries.add(new CountryCode("MR", "MAURITANIA"));
      countries.add(new CountryCode("MU", "MAURITIUS"));
      countries.add(new CountryCode("YT", "MAYOTTE"));
      countries.add(new CountryCode("MX", "MEXICO"));
      countries.add(new CountryCode("FM", "MICRONESIA, FEDERATED STATES OF"));
      countries.add(new CountryCode("MD", "MOLDOVA, REPUBLIC OF"));
      countries.add(new CountryCode("MC", "MONACO"));
      countries.add(new CountryCode("MN", "MONGOLIA"));
      countries.add(new CountryCode("ME", "MONTENEGRO"));
      countries.add(new CountryCode("MS", "MONTSERRAT"));
      countries.add(new CountryCode("MA", "MOROCCO"));
      countries.add(new CountryCode("MZ", "MOZAMBIQUE"));
      countries.add(new CountryCode("MM", "MYANMAR"));
      countries.add(new CountryCode("NA", "NAMIBIA"));
      countries.add(new CountryCode("NR", "NAURU"));
      countries.add(new CountryCode("NP", "NEPAL"));
      countries.add(new CountryCode("NL", "NETHERLANDS"));
      countries.add(new CountryCode("NC", "NEW CALEDONIA"));
      countries.add(new CountryCode("NZ", "NEW ZEALAND"));
      countries.add(new CountryCode("NI", "NICARAGUA"));
      countries.add(new CountryCode("NE", "NIGER"));
      countries.add(new CountryCode("NG", "NIGERIA"));
      countries.add(new CountryCode("NU", "NIUE"));
      countries.add(new CountryCode("NF", "NORFOLK ISLAND"));
      countries.add(new CountryCode("MP", "NORTHERN MARIANA ISLANDS"));
      countries.add(new CountryCode("NO", "NORWAY"));
      countries.add(new CountryCode("OM", "OMAN"));
      countries.add(new CountryCode("PK", "PAKISTAN"));
      countries.add(new CountryCode("PW", "PALAU"));
      countries.add(new CountryCode("PS", "PALESTINE, STATE OF"));
      countries.add(new CountryCode("PA", "PANAMA"));
      countries.add(new CountryCode("PG", "PAPUA NEW GUINEA"));
      countries.add(new CountryCode("PY", "PARAGUAY"));
      countries.add(new CountryCode("PE", "PERU"));
      countries.add(new CountryCode("PH", "PHILIPPINES"));
      countries.add(new CountryCode("PN", "PITCAIRN"));
      countries.add(new CountryCode("PL", "POLAND"));
      countries.add(new CountryCode("PT", "PORTUGAL"));
      countries.add(new CountryCode("PR", "PUERTO RICO"));
      countries.add(new CountryCode("QA", "QATAR"));
      countries.add(new CountryCode("RE", "RÉUNION"));
      countries.add(new CountryCode("RO", "ROMANIA"));
      countries.add(new CountryCode("RU", "RUSSIAN FEDERATION"));
      countries.add(new CountryCode("RW", "RWANDA"));
      countries.add(new CountryCode("BL", "SAINT BARTHÉLEMY"));
      countries.add(new CountryCode("SH", "SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA"));
      countries.add(new CountryCode("KN", "SAINT KITTS AND NEVIS"));
      countries.add(new CountryCode("LC", "SAINT LUCIA"));
      countries.add(new CountryCode("MF", "SAINT MARTIN (FRENCH PART)"));
      countries.add(new CountryCode("PM", "SAINT PIERRE AND MIQUELON"));
      countries.add(new CountryCode("VC", "SAINT VINCENT AND THE GRENADINES"));
      countries.add(new CountryCode("WS", "SAMOA"));
      countries.add(new CountryCode("SM", "SAN MARINO"));
      countries.add(new CountryCode("ST", "SAO TOME AND PRINCIPE"));
      countries.add(new CountryCode("SA", "SAUDI ARABIA"));
      countries.add(new CountryCode("SN", "SENEGAL"));
      countries.add(new CountryCode("RS", "SERBIA"));
      countries.add(new CountryCode("SC", "SEYCHELLES"));
      countries.add(new CountryCode("SL", "SIERRA LEONE"));
      countries.add(new CountryCode("SG", "SINGAPORE"));
      countries.add(new CountryCode("SX", "SINT MAARTEN (DUTCH PART)"));
      countries.add(new CountryCode("SK", "SLOVAKIA"));
      countries.add(new CountryCode("SI", "SLOVENIA"));
      countries.add(new CountryCode("SB", "SOLOMON ISLANDS"));
      countries.add(new CountryCode("SO", "SOMALIA"));
      countries.add(new CountryCode("ZA", "SOUTH AFRICA"));
      countries.add(new CountryCode("GS", "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"));
      countries.add(new CountryCode("SS", "SOUTH SUDAN"));
      countries.add(new CountryCode("ES", "SPAIN"));
      countries.add(new CountryCode("LK", "SRI LANKA"));
      countries.add(new CountryCode("SD", "SUDAN"));
      countries.add(new CountryCode("SR", "SURINAME"));
      countries.add(new CountryCode("SJ", "SVALBARD AND JAN MAYEN"));
      countries.add(new CountryCode("SZ", "SWAZILAND"));
      countries.add(new CountryCode("SE", "SWEDEN"));
      countries.add(new CountryCode("CH", "SWITZERLAND"));
      countries.add(new CountryCode("SY", "SYRIAN ARAB REPUBLIC"));
      countries.add(new CountryCode("TW", "TAIWAN, PROVINCE OF CHINA"));
      countries.add(new CountryCode("TJ", "TAJIKISTAN"));
      countries.add(new CountryCode("TZ", "TANZANIA, UNITED REPUBLIC OF"));
      countries.add(new CountryCode("TH", "THAILAND"));
      countries.add(new CountryCode("TL", "TIMOR-LESTE"));
      countries.add(new CountryCode("TG", "TOGO"));
      countries.add(new CountryCode("TK", "TOKELAU"));
      countries.add(new CountryCode("TO", "TONGA"));
      countries.add(new CountryCode("TT", "TRINIDAD AND TOBAGO"));
      countries.add(new CountryCode("TN", "TUNISIA"));
      countries.add(new CountryCode("TR", "TURKEY"));
      countries.add(new CountryCode("TM", "TURKMENISTAN"));
      countries.add(new CountryCode("TC", "TURKS AND CAICOS ISLANDS"));
      countries.add(new CountryCode("TV", "TUVALU"));
      countries.add(new CountryCode("UG", "UGANDA"));
      countries.add(new CountryCode("UA", "UKRAINE"));
      countries.add(new CountryCode("AE", "UNITED ARAB EMIRATES"));
      countries.add(new CountryCode("GB", "UNITED KINGDOM"));
      countries.add(new CountryCode("US", "UNITED STATES"));
      countries.add(new CountryCode("UM", "UNITED STATES MINOR OUTLYING ISLANDS"));
      countries.add(new CountryCode("UY", "URUGUAY"));
      countries.add(new CountryCode("UZ", "UZBEKISTAN"));
      countries.add(new CountryCode("VU", "VANUATU"));
      countries.add(new CountryCode("VE", "VENEZUELA, BOLIVARIAN REPUBLIC OF"));
      countries.add(new CountryCode("VN", "VIET NAM"));
      countries.add(new CountryCode("VG", "VIRGIN ISLANDS, BRITISH"));
      countries.add(new CountryCode("VI", "VIRGIN ISLANDS, U.S."));
      countries.add(new CountryCode("WF", "WALLIS AND FUTUNA"));
      countries.add(new CountryCode("EH", "WESTERN SAHARA"));
      countries.add(new CountryCode("YE", "YEMEN"));
      countries.add(new CountryCode("ZM", "ZAMBIA"));
      countries.add(new CountryCode("ZW", "ZIMBABWE"));
      return countries;
   }
}
