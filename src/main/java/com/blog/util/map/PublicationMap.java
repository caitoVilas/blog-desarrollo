package com.blog.util.map;

import com.blog.api.models.responses.PublicationResponse;
import com.blog.domain.entities.Publication;

/**
 * @author claudio.vilas
 * date 10/2023
 */

public class PublicationMap {
    public static PublicationResponse mapToDto(Publication publication){
        return PublicationResponse.builder()
                .id(publication.getId())
                .title(publication.getTitle())
                .description(publication.getDescription())
                .createAt(publication.getCreateAt())
                .updatedAt(publication.getUpdatedAt())
                .user(UserMap.mapToDto(publication.getUser()))
                .document(publication.getDocument())
                .build();
    }
}
