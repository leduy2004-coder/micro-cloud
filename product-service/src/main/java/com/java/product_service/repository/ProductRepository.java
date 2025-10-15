package com.java.product_service.repository;


import com.java.product_service.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    Page<ProductEntity> findAllByCategoryId(String categoryId, Pageable pageable);
    Page<ProductEntity> findAllByUserId(String userId, Pageable pageable);
    @Query("{ "
            + " $and: [ "
            + "   { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { ?0: null } ] }, "
            + "   { $or: [ { 'price': { $gte: ?1 } }, { ?1: null } ] }, "
            + "   { $or: [ { 'price': { $lte: ?2 } }, { ?2: null } ] }, "
            + "   { $or: [ { 'categoryId': ?3 }, { ?3: null } ] } "
            + " ] "
            + "}")
    Page<ProductEntity> searchProducts(
            String name,
            Double minPrice,
            Double maxPrice,
            String categoryId,
            Pageable pageable
    );

}