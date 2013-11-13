package org.giavacms.paypalrs.producer;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypalrs.model.PaypalConfiguration;
import org.giavacms.paypalrs.repository.PaypalConfigurationRepository;

@Named
@SessionScoped
public class PaypallProducer implements Serializable
{
   public PaypallProducer()
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
