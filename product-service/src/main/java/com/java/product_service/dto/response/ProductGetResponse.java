package com.java.product_service.dto.response;

import com.java.common_dto.CloudinaryResponse;
import com.java.common_dto.ProfileGetResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductGetResponse {
    String id;
    Double price;

    List<CloudinaryResponse> imgUrl;
    String categoryId;
    String name;
    String description;

    ProfileGetResponse user;
}