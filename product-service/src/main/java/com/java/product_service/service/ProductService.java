package com.java.product_service.service;


import com.java.common_dto.CloudinaryResponse;
import com.java.common_dto.FileDeleteAllRequest;
import com.java.product_service.dto.PageResponse;
import com.java.product_service.dto.request.ProductCreateRequest;
import com.java.product_service.dto.request.ProductUpdateImageRequest;
import com.java.product_service.dto.request.ProductUpdateRequest;
import com.java.product_service.dto.response.ProductCreateResponse;
import com.java.product_service.dto.response.ProductGetResponse;
import com.java.product_service.entity.ProductEntity;
import com.java.product_service.exception.AppException;
import com.java.product_service.exception.ErrorCode;
import com.java.product_service.repository.ProductRepository;
import com.java.product_service.repository.httpClient.FileClient;
import com.java.product_service.repository.httpClient.ProfileClient;
import com.java.product_service.utility.GetInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {

    ProductRepository productRepository;
    ModelMapper modelMapper;
    FileClient fileClient;
    ProfileClient profileClient;

    public ProductGetResponse getProductById(String productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return mapToProductGetResponse(product);
    }
    public PageResponse<ProductGetResponse> getMyProducts(int page, int size) {
        String userId = GetInfo.getLoggedInUserName();
        if (userId == null) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return getProductsByUserId(page, size, userId);
    }


    public PageResponse<ProductGetResponse> getProductsByUserId(int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        var resultPage = productRepository.findAllByUserId(userId, pageable);

        List<ProductGetResponse> data = resultPage.getContent()
                .stream().map(this::mapToProductGetResponse)
                .toList();

        return new PageResponse<>(page, resultPage.getSize(), resultPage.getTotalElements(),resultPage.getTotalPages(),  data);
    }
    public PageResponse<ProductGetResponse> getProductsByCategoryId(int page, int size, String categoryId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        var resultPage = productRepository.findAllByCategoryId(categoryId, pageable);

        List<ProductGetResponse> data = resultPage.getContent()
                .stream().map(this::mapToProductGetResponse)
                .toList();

        return new PageResponse<>(page, resultPage.getSize(), resultPage.getTotalElements(),resultPage.getTotalPages(),  data);
    }

    public PageResponse<ProductGetResponse> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        var resultPage = productRepository.findAll(pageable);

        List<ProductGetResponse> data = resultPage.getContent()
                .stream().map(this::mapToProductGetResponse)
                .toList();

        return new PageResponse<>(page, resultPage.getSize(), resultPage.getTotalElements(),resultPage.getTotalPages(),  data);
    }
    public PageResponse<ProductGetResponse> searchProducts(
            int page,
            int size,
            String name,
            Double minPrice,
            Double maxPrice,
            String categoryId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ProductEntity> products = productRepository.searchProducts(name, minPrice, maxPrice, categoryId, pageable);

        List<ProductGetResponse> data = products.stream()
                .map(this::mapToProductGetResponse)
                .toList();
        return new PageResponse<>(page, products.getSize(), products.getTotalElements(),products.getTotalPages(), data);

    }

    public ProductCreateResponse createProduct(ProductCreateRequest request, List<MultipartFile> files) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            ProductEntity entity = ProductEntity.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .description(request.getDescription())
                    .userId(userId)
                    .categoryId(request.getCategoryId())
                    .build();

            entity = productRepository.save(entity);
            String productId = entity.getId();

            List<CloudinaryResponse> imgUrls = files.stream()
                    .map(file -> fileClient.uploadMediaProduct(file, productId).getResult())
                    .toList();

            ProductCreateResponse response = modelMapper.map(entity, ProductCreateResponse.class);
            response.setImgUrl(imgUrls);

            return response;

        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public ProductGetResponse updateProduct(ProductUpdateRequest request){
        ProductEntity entity = productRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ProductEntity product = modelMapper.map(request, ProductEntity.class);
        product.setUserId(GetInfo.getLoggedInUserName());

        ProductEntity productUpdate = productRepository.save(product);

        return mapToProductGetResponse(productUpdate);
    }

    public List<CloudinaryResponse> updateImageProduct(ProductUpdateImageRequest request, List<MultipartFile> files) {
        //remove image

        fileClient.deleteAllImageProduct(FileDeleteAllRequest.builder().id(request.getProductId()).build());

        //upload image
        return files.stream()
                .map(file -> fileClient.uploadMediaProduct(file, request.getProductId()).getResult())
                .toList();

    }

    public boolean deleteProduct(String productId) {
        try {
            productRepository.deleteById(productId);
            fileClient.deleteAllImageProduct(FileDeleteAllRequest.builder().id(productId).build());
            return true;
        } catch (Exception e) {
            log.error("Error deleting product by admin", e);
            return false;
        }
    }

    private ProductGetResponse mapToProductGetResponse(ProductEntity entity) {
        try {
            ProductGetResponse response = modelMapper.map(entity, ProductGetResponse.class);

            response.setImgUrl(fileClient.getImage(response.getId()).getResult());

            response.setUser(profileClient.getProfile(entity.getUserId()).getResult());

            return response;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
