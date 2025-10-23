package com.java.product_service.repository;

import com.java.product_service.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {
    Page<CommentEntity> findAllByProductId(String productId, Pageable pageable);
    void deleteAllByProductId(String productId);
    @Query("{ '_id': ?0, 'userId': ?1 }")
    CommentEntity findByIdAndUserId(String id, String userId);

    List<CommentEntity> findAllByParentId(String parentId);
}
