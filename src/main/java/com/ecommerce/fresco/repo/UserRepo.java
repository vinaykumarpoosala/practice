package com.ecommerce.fresco.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.ecommerce.fresco.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>{

	User findByUsername(String username);

}
