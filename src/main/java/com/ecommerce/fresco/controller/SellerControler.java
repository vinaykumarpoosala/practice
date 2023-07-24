package com.ecommerce.fresco.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.fresco.model.Product;
import com.ecommerce.fresco.model.User;
import com.ecommerce.fresco.repo.CartProductRepo;
import com.ecommerce.fresco.repo.ProductRepo;
import com.ecommerce.fresco.repo.UserRepo;

@RestController
@RequestMapping("api/auth/seller/")
@PreAuthorize("hasAuthority('SELLER')")
public class SellerControler {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	CartProductRepo cpRepo;
	
	@Autowired 
	UserRepo userRepo;
	
	@GetMapping("product")
	public ResponseEntity<?> getLoggedInSellerProducts(){
		
		String username = getUserName();
		User user = userRepo.findByUsername(username);
		
		List<Product> li = productRepo.findBySellerUserId(user.getUserId());
		
		if(li != null) {
			return new ResponseEntity<>(li,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		
	}
	
	
	@GetMapping("product/{productId}")
	public ResponseEntity<?> getLoggedInSellerProduct(@PathVariable("productId") Integer productId){
		
		String username = getUserName();
		User user = userRepo.findByUsername(username);
		
		Product li = productRepo.findBySellerUserIdAndProductId(user.getUserId(),productId);
		
		if(li != null) {
			return new ResponseEntity<>(li,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		
		
	}
	
	
	@PostMapping("product")
	public ResponseEntity<?> saveProductToSeller(@RequestBody Product product){
		
		String username = getUserName();
		User user = userRepo.findByUsername(username);
		product.setSeller(user);
		product = productRepo.save(product);
		
		String createdUri = "http://localhost:8000/api/auth/seller/product/" +product.getProductId(); // The URL you want to redirect to

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(createdUri));

        return new ResponseEntity<>(product , headers, HttpStatus.CREATED);
		
//		return new ResponseEntity<Object>(HttpStatus.CREATED); 
	}
	
	
	@PutMapping("product")
	public ResponseEntity<?> updateProductToSeller(@RequestBody Product product){
		
		String username = getUserName();
		User user = userRepo.findByUsername(username);
		
		Product prod = productRepo.findBySellerUserIdAndProductId(user.getUserId(), product.getProductId());
		if(prod == null) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND); 
		}
		
		product.setSeller(user);
		productRepo.save(product);
		
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}
	
	
	@DeleteMapping("product/{productId}")
	public ResponseEntity<?> delteProductToSeller(@PathVariable Integer productId){
		
		String username = getUserName();
		User user = userRepo.findByUsername(username);
		
		Product prod = productRepo.findBySellerUserIdAndProductId(user.getUserId(), productId);
		if(prod == null) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND); 
		}
		
		cpRepo.deleteByProductProductId(productId);
		productRepo.deleteById(productId);
		
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}
	
	public String getUserName() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("__________");
		System.out.println(auth);
		return auth.getName();

	}
}
