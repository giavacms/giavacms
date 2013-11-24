package org.giavacms.paypalweb.producer;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.repository.PaypalConfigurationRepository;

@Named
@SessionScoped
public class PaypalProducer implements Serializable
{
   public PaypalProducer()
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
