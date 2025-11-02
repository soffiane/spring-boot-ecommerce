package com.angular.springboot.ecommerce.dao;

import com.angular.springboot.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin("http://localhost:4200")
//Pas de @RepositoryRestResource car on ne veut pas exposer ce endpoint
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);
}
