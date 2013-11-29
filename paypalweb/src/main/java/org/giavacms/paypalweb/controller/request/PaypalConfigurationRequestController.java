package org.giavacms.paypalweb.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.repository.PaypalConfigurationRepository;

@Named
@RequestScoped
public class PaypalConfigurationRequestController implements Serializable
{
   public PaypalConfigurationRequestController()
   {
   }

   private static final long serialVersionUID = 1L;
   @Inject
   PaypalConfigurationRepository paypalConfigurationRepository;

   public PaypalConfiguration getPaypalConfiguration()
   {
      return paypalConfigurationRepository.load();
   }

}
