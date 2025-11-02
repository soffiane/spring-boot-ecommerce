package com.angular.springboot.ecommerce.dao;

import com.angular.springboot.ecommerce.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

//collectionResourceRel est le nom du json en entree et path permet de changer l'url dans l'API
@RepositoryRestResource(collectionResourceRel = "productCategory", path="product-category")
//@CrossOrigin("http://localhost:4200")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
