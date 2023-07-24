package com.ecommerce.fresco.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cart implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartId;
	
	
	private Double totalAmount;
	
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private User user;
	
	
	@OneToMany(mappedBy = "cart",fetch = FetchType.EAGER)
	private List<CartProduct> cartProducts;


	public Integer getCartId() {
		return cartId;
	}


	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}


	public Double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public List<CartProduct> getCartProducts() {
		return cartProducts;
	}


	public void setCartProducts(List<CartProduct> cartProducts) {
		this.cartProducts = cartProducts;
	}


	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Cart(Integer cartId, Double totalAmount, User user, List<CartProduct> cartProducts) {
		super();
		this.cartId = cartId;
		this.totalAmount = totalAmount;
		this.user = user;
		this.cartProducts = cartProducts;
	}
	
	
	
	
	
	
}

