package com.java.common_dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationResponse {
    String id;
    String userId;
    String email;
    Date dateOfBirth;
    Boolean gender;
    String nickName;
}
