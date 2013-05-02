package org.giavacms.ctp.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Product;
import org.giavacms.ctp.repository.CustomerToProductRepository;
import org.giavacms.customer.model.Customer;

@Named
@RequestScoped
public class CustomerToProductRequestController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    CustomerToProductRepository customerToProductRepository;

    public CustomerToProductRequestController() {
        super();
    }

    public List<Product> getProductList(Customer customer) {
        List<Product> l = customerToProductRepository.getProductList(customer);
        return l;
    }

    
}
