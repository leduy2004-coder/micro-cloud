package com.java.file_service.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document // Đánh dấu đây là một MongoDB document (nếu là entity độc lập)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class BaseEntity {

    @MongoId // Định danh ID trong MongoDB
    private String id;

    @CreatedDate
    @Field("created_date")
    private LocalDateTime createdDate;


    @LastModifiedDate
    @Field("modified_date")
    private LocalDateTime modifiedDate;
}
