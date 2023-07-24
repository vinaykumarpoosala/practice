package com.ecommerce.fresco.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.fresco.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
