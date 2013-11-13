package org.giavacms.paypalrs.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PayerInfo implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String email;
   private String firstName;
   private String lastName;
   private String payerId;
   private String phone;
   private ShippingAddress shippingAddress;
   private Address address;

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

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
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

   public String getPayerId()
   {
      return payerId;
   }

   public void setPayerId(String payerId)
   {
      this.payerId = payerId;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   public ShippingAddress getShippingAddress()
   {
      if (this.shippingAddress == null)
         this.shippingAddress = new ShippingAddress();
      return shippingAddress;
   }

   public void setShippingAddress(ShippingAddress shippingAddress)
   {
      this.shippingAddress = shippingAddress;
   }

   @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   public Address getAddress()
   {
      if (this.address == null)
         this.address = new Address();
      return address;
   }

   public void setAddress(Address address)
   {
      this.address = address;
   }

   @Override
   public String toString()
   {
      return "PayerInfo [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
               + ", payerId=" + payerId + ", phone=" + phone + ", address=" + address + "]";
   }

}
