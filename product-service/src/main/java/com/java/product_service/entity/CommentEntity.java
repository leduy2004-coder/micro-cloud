package com.java.product_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@Document(value = "comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseEntity {

    String parentId;

    String productId;

    String comment;
    String userId;


}