package org.giavacms.paypalweb.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.servlet.IpnInfo;

@Stateless
@LocalBean
public class ShoppingCartService
{

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   public IpnInfo getIpnInfo(String id)
   {

      return new IpnInfo();
   }

}
