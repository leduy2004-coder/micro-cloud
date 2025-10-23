package com.java.product_service.dto.response;

import com.java.common_dto.ProfileGetResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentGetResponse {
    String id;
    String comment;
    String parentId;
    String created;
    ProfileGetResponse user;
}
