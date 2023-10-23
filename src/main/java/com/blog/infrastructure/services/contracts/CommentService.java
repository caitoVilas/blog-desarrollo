package com.blog.infrastructure.services.contracts;

import com.blog.api.models.requests.CommentRequest;
import com.blog.api.models.responses.CommentResponse;
import com.blog.util.enums.SortType;
import org.springframework.data.domain.Page;

/**
 * @author claudio.vilas
 * date 10/2023
 */

public interface CommentService {
    CommentResponse create(CommentRequest request);
    CommentResponse getById(Long id);
    Page<CommentResponse> getByPublication(int page, int size, SortType sortType, Long publicationId);
    void delete(Long id);
}
