package com.blog.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentRequest implements Serializable {
    private Long publicationId;
    private Long userId;
    private String comment;
}
