package com.ecommerce.fresco.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.fresco.model.Product;
import com.ecommerce.fresco.model.User;
import com.ecommerce.fresco.repo.ProductRepo;
import com.ecommerce.fresco.repo.UserRepo;
import com.ecommerce.fresco.security.CustomUserdetailsService;
import com.ecommerce.fresco.security.JwtUtil;

@RestController
@RequestMapping("api/public/")
public class PublicController {

	
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepo repo;
	
	
	@Autowired
	CustomUserdetailsService userdetailsService;
	
	
	
	@Autowired
	JwtUtil jwtUtil;
	
	
	@GetMapping("all")
	public List<User> getUsers(){
		return repo.findAll();
		
	}
	

	@Autowired
	ProductRepo prodRepo;
	
	@GetMapping("product/search")
	public List<Product> searchProducts(@RequestParam("keyword") String keyword){
		
		return prodRepo.findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(keyword, keyword);
		
	}
	
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user){
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	
		}catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Username or password is incorrect");
		}
		
		UserDetails userDetails = userdetailsService.loadUserByUsername(user.getUsername());
		
		
		String token = jwtUtil.generateToken(userDetails);
		
		
		
		
		
		return new ResponseEntity<String>(token,HttpStatus.OK);
		
	}
}
