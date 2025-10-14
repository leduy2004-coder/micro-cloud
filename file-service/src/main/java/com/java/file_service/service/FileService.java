package com.java.file_service.service;


import com.java.common_dto.CloudinaryResponse;
import com.java.file_service.entity.ProductImageEntity;
import com.java.file_service.repository.ProductRepository;
import com.java.file_service.utility.ImageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    CloudinaryService cloudinaryService;
    ProductRepository productRepository;

    public CloudinaryResponse uploadFile(MultipartFile file, String id) {
        ImageUtils.assertAllowed(file, ImageUtils.IMAGE_PATTERN);
        String fileName = ImageUtils.getFileName(file.getOriginalFilename());
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
        String imgId = productRepository.save(ProductImageEntity.builder()
                    .productId(id)
                    .name(fileName)
                    .url(response.getUrl())
                    .publicId(response.getPublicId())
                    .build()).getId();

        return CloudinaryResponse.builder()
                .id(imgId)
                .url(response.getUrl()).build();
    }


    public List<CloudinaryResponse> getAllById(String id) {

        var list = productRepository.findAllByProductId(id);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.stream()
                    .map(product -> CloudinaryResponse.builder()
                            .id(product.getId())
                            .url(product.getUrl())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public Boolean deleteAllById(String id) {

            var list = productRepository.findAllByProductId(id);
            if (list.isEmpty()) {
                return false;
            }
            list.forEach(product -> {
                // Xóa ảnh khỏi Cloudinary
                if (product.getPublicId() != null) {
                    cloudinaryService.deleteFile(product.getPublicId(), "image");
                }
                productRepository.deleteById(product.getId());
            });

            return true;
        }


    public Boolean deleteById(List<String> idsImage) {
        if (idsImage == null || idsImage.isEmpty()) {
            return false;
        }

        for (String id : idsImage) {
            ProductImageEntity image = productRepository.findById(id).orElse(null);
            if (image == null) {
                return false;
            }
            cloudinaryService.deleteFile(image.getPublicId(), "image");
        }

        return true;
    }

}