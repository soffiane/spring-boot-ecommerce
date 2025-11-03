package com.angular.springboot.ecommerce.config;

import com.angular.springboot.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final EntityManager entityManager;

    @Value("${allowed.origins}")
    private String[] origins;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        disableHttpMethod(config, unsupportedActions, Product.class);
        disableHttpMethod(config, unsupportedActions, ProductCategory.class);
        disableHttpMethod(config, unsupportedActions, Country.class);
        disableHttpMethod(config, unsupportedActions, State.class);
        disableHttpMethod(config, unsupportedActions, Order.class);
        //call an internal method to expose the ids
        exposeIds(config);
        //autorise le cross origin pour nos endpoints - on peut retirer l'annotation dans le controller et les repository
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(origins);
    }

    //on veut empecher de supprimer ou modifier des pays des produits et des categories
    private static void disableHttpMethod(RepositoryRestConfiguration config, HttpMethod[] unsupportedActions, Class theClass) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metadata, httpMethod) -> httpMethod.disable(unsupportedActions))
                .withCollectionExposure((metadata, httpMethod) -> httpMethod.disable(unsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //recupere la liste de toutes les entités geré par JPA
        /*Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        //on cree un tableaux de ces entites
        List<Class> entityClasses = new ArrayList<>();
        //on recupere les types d'objets
        for (EntityType entity : entities) {
            entityClasses.add(entity.getJavaType());
        }
        //on expose les id de ces entités
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);*/
        Class[] classes = entityManager.getMetamodel()
                .getEntities().stream().map(Type::getJavaType).toArray(Class[]::new);
        config.exposeIdsFor(classes);

    }


}
