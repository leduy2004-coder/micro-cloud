package com.java.product_service.service;



import com.java.common_dto.CloudinaryResponse;
import com.java.product_service.dto.request.CategoryCreateRequest;
import com.java.product_service.dto.response.CategoriesResponse;
import com.java.product_service.entity.CategoriesEntity;
import com.java.product_service.exception.AppException;
import com.java.product_service.exception.ErrorCode;
import com.java.product_service.repository.CategoryRepository;
import com.java.product_service.repository.httpClient.FileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;
    FileClient fileClient;


    public List<CategoriesResponse> getAllCategory() {
        List<CategoriesEntity> categories = categoryRepository.findAll();
        return categories.stream().map(categoriesEntity -> {
            CategoriesResponse categoryResponse = modelMapper.map(categoriesEntity, CategoriesResponse.class);
            var response = fileClient.getImage(categoriesEntity.getId());
            if (response != null &&
                    response.getResult() != null &&
                    !response.getResult().isEmpty()) {

                CloudinaryResponse img = response.getResult().getFirst();
                categoryResponse.setImg(img);
            }
            return categoryResponse;
        }).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoriesResponse createCategory(CategoryCreateRequest request, MultipartFile file) {


        CategoriesEntity category = modelMapper.map(request, CategoriesEntity.class);
        category = categoryRepository.save(category);

        CloudinaryResponse img =  fileClient.uploadMediaProduct(file, category.getId()).getResult();
        CategoriesResponse categoryResponse = modelMapper.map(category, CategoriesResponse.class);
        categoryResponse.setImg(img);
        return categoryResponse;
    }

}