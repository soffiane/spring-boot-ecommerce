package com.angular.springboot.ecommerce.config;

import com.angular.springboot.ecommerce.entity.Product;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * On va creer une configuration pour empecher la creation ou la suppression de produits
 * en desactivant le PUT POST et DELETE pour les endpoints product
 */
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metadata,httpMethod) -> httpMethod.disable(unsupportedActions))
                .withCollectionExposure((metadata,httpMethod) -> httpMethod.disable(unsupportedActions));
    }
}
