package com.java.product_service.dto.response;

import com.java.common_dto.CloudinaryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesResponse {
    String id;
    String name;
    CloudinaryResponse img;
}