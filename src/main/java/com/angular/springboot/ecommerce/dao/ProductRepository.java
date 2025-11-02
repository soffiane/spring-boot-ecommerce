package com.angular.springboot.ecommerce.dao;

import com.angular.springboot.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin("http://localhost:4200")
@RepositoryRestResource()
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Spring Data JPA query methods
     * methodes qui commencent par findBy..., readBy.... , findAllBy..... supporte aussi and, or, like, sort....
     * Ici on veut les produits par categorie de produit
     * Pageable et Page pour la pagination des resultats
     * SELECT * FROM product where category_id = ?;
     * On peut creer ses propres query methodes avec @Query
     * disponible sous le endpoint : /search/findByCategoryId?id={id}&page={page}&size={size}
     */
    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);
    //equivaut a like '%%' coté SQL
    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
}
