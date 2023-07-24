package com.ecommerce.fresco.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.fresco.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>{

	public List<Product> findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(String productName , String categoryName);

	public List<Product> findBySellerUserId(Integer sellerId);

	public Product findBySellerUserIdAndProductId(Integer userId,Integer productId);
}
