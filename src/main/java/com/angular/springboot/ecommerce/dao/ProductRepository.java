package com.angular.springboot.ecommerce.dao;

import com.angular.springboot.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
