package com.angular.springboot.ecommerce.dao;

import com.angular.springboot.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

//on autorise l'execution de code javascript depuis le navigateur en provenance de cette origine - CORS
@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product, Long> {
}
