package com.java.product_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@Document(value = "product")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity {
    String userId;
    String name;
    long price;
    String description;
    String categoryId;
}