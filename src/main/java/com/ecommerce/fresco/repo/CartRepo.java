package com.ecommerce.fresco.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.fresco.model.Cart;


@Repository
public interface CartRepo extends JpaRepository<Cart, Integer>{

	
	public Cart findByUserUsername(String username);
}
