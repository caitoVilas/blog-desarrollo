package com.blog.util.map;

import com.blog.api.models.responses.CommentResponse;
import com.blog.domain.entities.Comment;

/**
 * @author claudio.vilas
 * date 10/2023
 */

public class CommentMap {
    public static CommentResponse mapToDto(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .publication(PublicationMap.mapToDto(comment.getPublication()))
                .user(UserMap.mapToDto(comment.getUser()))
                .comment(comment.getComment())
                .createAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .build();
    }
}
