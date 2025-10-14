package com.java.product_service.repository;


import com.java.product_service.entity.CategoriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<CategoriesEntity, String> {

}