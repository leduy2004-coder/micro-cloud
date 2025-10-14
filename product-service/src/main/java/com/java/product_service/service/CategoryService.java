package com.java.product_service.service;



import com.java.product_service.dto.request.CategoryCreateRequest;
import com.java.product_service.dto.response.CategoriesResponse;
import com.java.product_service.entity.CategoriesEntity;
import com.java.product_service.exception.AppException;
import com.java.product_service.exception.ErrorCode;
import com.java.product_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;


    public CategoriesResponse getCategoryById(String categoryId) {
        CategoriesEntity category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        return modelMapper.map(category, CategoriesResponse.class);
    }

    public List<CategoriesResponse> getAllCategory() {
        List<CategoriesEntity> categories = categoryRepository.findAll();
        return categories.stream().map(categoriesEntity ->
                modelMapper.map(categoriesEntity, CategoriesResponse.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoriesResponse createCategory(CategoryCreateRequest request) {

        CategoriesEntity category = modelMapper.map(request, CategoriesEntity.class);
        category = categoryRepository.save(category);

        return modelMapper.map(category, CategoriesResponse.class);
    }
}