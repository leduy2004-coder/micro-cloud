package com.java.product_service.repository;


import com.java.product_service.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    Page<ProductEntity> findAllByUserId(String userId, Pageable pageable);

}