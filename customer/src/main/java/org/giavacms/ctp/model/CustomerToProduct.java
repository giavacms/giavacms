package org.giavacms.ctp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.giavacms.catalogue.model.Product;
import org.giavacms.customer.model.Customer;

/**
 * @author alessandro prandini
 *
 */
@Entity
public class CustomerToProduct {

    private Long id;
    private Customer customer;
    private Product product;
    private Integer listOrder = 0;

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return
     */
    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return
     */
    @ManyToOne
    public Product getProduct() {
        return product;
    }

    /**
     * @param product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

	/**
	 * @return the listOrder
	 */
	public Integer getListOrder() {
		return listOrder;
	}

	/**
	 * @param listOrder the listOrder to set
	 */
	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}

}
