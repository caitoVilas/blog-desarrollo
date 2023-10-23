package com.blog.api.controllers;

import com.blog.api.models.requests.CommentRequest;
import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.CommentResponse;
import com.blog.api.models.responses.PublicationResponse;
import com.blog.api.models.responses.UserResponse;
import com.blog.infrastructure.services.contracts.CommentService;
import com.blog.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@RestController
@RequestMapping("/api/v1/blog/comments")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "BLOG - COMMENTS")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "servicio para crear comentarios",
            description = "servicio para crear comentarios")
    @Parameter(name = "request", description = "datos del comentario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest request){
        log.info("#### endpoint para crear comentarios ####");
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "servicio para consulta de comentarios por id",
            description = "servicio para consulta de comentarios por id si existe")
    @Parameter(name = "id", description = "id del comentario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<CommentResponse> getById(@PathVariable Long id){
        log.info("#### endpoint consulta comentarios por id ####");
        return ResponseEntity.ok(commentService.getById(id));
    }

    @GetMapping("/publication")
    @Operation(summary = "servicio para consulta de comentarios por publicacion",
            description = "servicio para ponsulta de comentarios por publicacion")
    @Parameters({
            @Parameter(name = "page", description = "numero de pagina"),
            @Parameter(name = "size", description = "cantidad de elementos"),
            @Parameter(name = "sortBy", description = "ordenamiento"),
            @Parameter(name = "publicationId", description = "id de la publicacion")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<Page<CommentResponse>> getByTitle(@RequestParam int page,
                                                                @RequestParam int size,
                                                                @RequestParam SortType sortType,
                                                                @RequestParam Long publicationId){
        log.info("#### endpoint consulta de comentarios por publicacion ####");
        var coments = commentService.getByPublication(page, size, sortType, publicationId);
        if (coments.getContent().isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(coments);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "servicio para eliminacion de comentarios por id",
            description = "servicio para eliminacion de comentarios por id si existe")
    @Parameter(name = "id", description = "id del comentario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<?> delete(@PathVariable Long id){
        log.info("#### endpoint eliminacion de comentarios por id ####");
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
