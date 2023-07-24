package com.ecommerce.fresco.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.fresco.model.Cart;
import com.ecommerce.fresco.model.CartProduct;
import com.ecommerce.fresco.model.Product;
import com.ecommerce.fresco.repo.CartProductRepo;
import com.ecommerce.fresco.repo.CartRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/auth/consumer/")
@PreAuthorize("hasAuthority('CONSUMER')")
public class ConsumerController {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartProductRepo cartProductRepo;

	@GetMapping("cart")
	public ResponseEntity<?> getCartforLoggedInUser() throws JsonProcessingException {

		System.out.println("inside cart method");

		Cart cart = cartRepo.findByUserUsername(getUserName());

		// Create an instance of the ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		// Serialize the Cart object to JSON
		String cartJson = objectMapper.writeValueAsString(cart);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);

	}

	@PostMapping("/cart")
	public ResponseEntity<?> addProductToCart(@RequestBody Product product) {
		String username = getUserName();
		Cart cart = cartRepo.findByUserUsername(username);

		Optional<CartProduct> existingCartProduct = cartProductRepo
				.findByProductProductIdAndCartUserUserId(product.getProductId(), cart.getCartId());

		if (existingCartProduct.isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		CartProduct cartProduct = new CartProduct();
		cartProduct.setCart(cart);
		cartProduct.setProduct(product);

		cartProduct.setCart(cart);

		cartProductRepo.save(cartProduct);
		cart.getCartProducts().add(cartProduct);
		cartRepo.save(cart);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("cart")
	public ResponseEntity<?> updateCart(@RequestBody CartProduct cartProduct) {
		String username = getUserName();

		Cart cart = cartRepo.findByUserUsername(username);
		
		List<CartProduct> liCart = cart.getCartProducts();
		Optional<CartProduct> cartPro = cartProductRepo
				.findByProductProductIdAndCartCartId(cartProduct.getProduct().getProductId(), cart.getCartId());

		if (cartPro.isEmpty() && cartProduct.getQuantity() == 0) {
			return ResponseEntity.ok(HttpStatus.OK);
			
			
		} 
		else if(cartPro.isEmpty()){
			cartProduct.setCart(cart);
			cartProductRepo.save(cartProduct);
			cartRepo.save(cart);}
			else {
		


			if (cartProduct.getQuantity() == 0) {
				cartProductRepo.deleteByCartUserUserIdAndProductProductId(cart.getCartId(),
						cartProduct.getProduct().getProductId());
				
			} else {
				cartProduct.setQuantity(cartProduct.getQuantity());
				cartProduct.setCart(cart);

				
				cartProductRepo.save(cartProduct);
				cartRepo.save(cart);

			}

		}

	return ResponseEntity.ok(HttpStatus.OK);

	}

	@DeleteMapping("cart")
	public ResponseEntity<?> deleteProductFromCart(@RequestBody Product product) {
		String username = getUserName();

		Cart cart = cartRepo.findByUserUsername(username);

		List<CartProduct> cartProducts = cart.getCartProducts();
		
		System.out.println(cartProducts.size());
		cartProducts.removeIf(obj -> obj.equals(product));
		System.out.println(cartProducts.size());

		cart.setCartProducts(cartProducts);
//		cartProductRepo.deleteByCartUserUserIdAndProductProductId(cart.getUser().getUserId(), product.getProductId());

		cartRepo.save(cart);
		return new ResponseEntity<Object>(HttpStatus.OK);

	}

	public String getUserName() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("__________");
		System.out.println(auth);
		return auth.getName();

	}
}
