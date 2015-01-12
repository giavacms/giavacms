/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = ShippingAddress.TABLE_NAME)
public class ShippingAddress implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   public static final String TABLE_NAME = "PPW_ShippingAddress";

   private String firstName;
   private String lastName;
   private String line1;
   private String line2;
   private String city;
   private String countryCode;
   private String zip;
   private String state;

   public ShippingAddress()
   {
   }

   public ShippingAddress(String firstName, String lastName, String line1, String line2, String city,
            String countryCode, String zip, String state)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      this.line1 = line1;
      this.line2 = line2;
      this.city = city;
      this.countryCode = countryCode;
      this.zip = zip;
      this.state = state;
   }

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

   public String getLine1()
   {
      return line1;
   }

   public void setLine1(String line1)
   {
      this.line1 = line1;
   }

   public String getLine2()
   {
      return line2;
   }

   public void setLine2(String line2)
   {
      this.line2 = line2;
   }

   public String getCity()
   {
      return city;
   }

   public void setCity(String city)
   {
      this.city = city;
   }

   public String getCountryCode()
   {
      return countryCode;
   }

   public void setCountryCode(String countryCode)
   {
      this.countryCode = countryCode;
   }

   public String getState()
   {
      return state;
   }

   public void setState(String state)
   {
      this.state = state;
   }

   public String getFirstName()
   {
      return firstName;
   }

   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }

   public String getLastName()
   {
      return lastName;
   }

   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }

   public String getZip()
   {
      return zip;
   }

   public void setZip(String zip)
   {
      this.zip = zip;
   }

   @Override
   public String toString()
   {
      return "ShippingAddress [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", line1=" + line1
               + ", line2=" + line2 + ", city=" + city + ", countryCode=" + countryCode + ", zip=" + zip + ", state="
               + state + "]";
   }

}
