package com.angular.springboot.ecommerce.dao;

import com.angular.springboot.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Requete SQL generée par Spring Data API : SELECT * FROM Orders LEFT OUTER JOIN customer ON orders.customer_id = customer.id WHERE customer.email = :email
     * disponible sous le endpoint : /orders/search/findByCustomerEmail?email={email}&page={page}&size={size}
     * @param email
     * @param pageable
     * @return
     */
    Page<Order> findByCustomerEmail(@Param("email") String email, Pageable pageable);
}
