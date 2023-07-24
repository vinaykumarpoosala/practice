package com.ecommerce.fresco.repo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.fresco.model.CartProduct;

@Repository
public interface CartProductRepo  extends CrudRepository<CartProduct, Integer>{

	public Optional<CartProduct> findByProductProductIdAndCartCartId(Integer prodcutId , Integer cartId);

	@Transactional
	public void deleteByCartUserUserIdAndProductProductId(Integer userId,Integer prodcutId );

	public Optional<CartProduct> findByProductProductIdAndCartUserUserId(Integer productId, Integer cartId);

	@Transactional
	public void deleteByProductProductId(Integer productId);
}
