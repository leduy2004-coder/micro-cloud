package com.java.product_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreateResponse {
    String productId;
    String comment;
    String parentId;
    String id;
    String created;
}
