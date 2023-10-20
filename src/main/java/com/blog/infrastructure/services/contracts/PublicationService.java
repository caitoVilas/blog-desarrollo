package com.blog.infrastructure.services.contracts;

import com.blog.api.models.requests.PublicationRequest;
import com.blog.api.models.responses.PublicationResponse;
import com.blog.util.enums.SortType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author claudio.vilas
 * date 10/2023
 */

public interface PublicationService {
    PublicationResponse create(PublicationRequest request, MultipartFile file);
    PublicationResponse getById(Long id);
    Page<PublicationResponse> getByTitle(int page, int size, SortType sortType, String tile);
    Page<PublicationResponse> getAll(int page, int size, SortType sortType);

}
