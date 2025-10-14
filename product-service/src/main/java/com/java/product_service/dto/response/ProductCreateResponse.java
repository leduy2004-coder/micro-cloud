package com.java.product_service.dto.response;

import com.java.common_dto.CloudinaryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateResponse {
    String id;
    Double price;
    String description;
    String name;
    List<CloudinaryResponse> imgUrl;
}