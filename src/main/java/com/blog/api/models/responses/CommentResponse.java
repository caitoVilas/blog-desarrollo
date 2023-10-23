package com.blog.api.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentResponse implements Serializable {
    private Long id;
    private PublicationResponse publication;
    private UserResponse user;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate updateAt;
}
