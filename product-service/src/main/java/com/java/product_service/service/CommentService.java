package com.java.product_service.service;


import com.java.common_dto.ProfileGetResponse;
import com.java.product_service.dto.PageResponse;
import com.java.product_service.dto.request.CommentCreateRequest;
import com.java.product_service.dto.response.CommentCreateResponse;
import com.java.product_service.dto.response.CommentGetResponse;
import com.java.product_service.entity.CommentEntity;
import com.java.product_service.exception.AppException;
import com.java.product_service.exception.ErrorCode;
import com.java.product_service.repository.CommentRepository;
import com.java.product_service.repository.httpClient.ProfileClient;
import com.java.product_service.utility.GetInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {

    CommentRepository commentRepository;
    ModelMapper modelMapper;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;

    public CommentCreateResponse saveCommentProduct(CommentCreateRequest commentCreateRequest) {
        String userId = GetInfo.getLoggedInUserName();

        CommentEntity postCommentEntity = modelMapper.map(commentCreateRequest, CommentEntity.class);
        postCommentEntity.setUserId(userId);

        CommentEntity commentSave = commentRepository.save(postCommentEntity);


        return CommentCreateResponse.builder()
                .productId(commentSave.getProductId())
                .comment(commentCreateRequest.getComment())
                .created(dateTimeFormatter.format(commentSave.getCreatedDate()))
                .parentId(commentCreateRequest.getParentId())
                .id(commentSave.getId())
                .build();
    }

    public PageResponse<CommentGetResponse> getCommentProduct(int page, int size, String postId) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page-1, size, sort);

        var listComments = commentRepository.findAllByProductId(postId,pageable);
        List<CommentGetResponse> postList = listComments.getContent().stream().map(comment -> {
            var postResponse = modelMapper.map(comment, CommentGetResponse.class);
            postResponse.setCreated(dateTimeFormatter.format(comment.getCreatedDate()));
            ProfileGetResponse profile = profileClient.getProfile(comment.getUserId()).getResult();
            postResponse.setUser(profile);
            return postResponse;
        }).toList();

        return PageResponse.<CommentGetResponse>builder()
                .currentPage(page)
                .pageSize(listComments.getSize())
                .totalPages(listComments.getTotalPages())
                .totalElements(listComments.getTotalElements())
                .data(postList)
                .build();
    }
    public Boolean deleteComment(String commentId) {
        if(!GetInfo.isAdmin()) {
            CommentEntity entity = commentRepository.findByIdAndUserId(commentId, GetInfo.getLoggedInUserName());
            if (entity == null) {
                throw new AppException(ErrorCode.COMMENT_NOT_OWNED);
            }
        }

        List<CommentEntity> childComments = commentRepository.findAllByParentId(commentId);
        if (!childComments.isEmpty()) {
            List<String> childIds = childComments.stream()
                    .map(CommentEntity::getId)
                    .toList();
            commentRepository.deleteAllById(childIds);
        }

        commentRepository.deleteById(commentId);

        return true;
    }




}
