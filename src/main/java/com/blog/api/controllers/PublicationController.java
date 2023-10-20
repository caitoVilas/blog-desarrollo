package com.blog.api.controllers;

import com.blog.api.models.requests.PublicationRequest;
import com.blog.api.models.responses.PublicationResponse;
import com.blog.api.models.responses.UserResponse;
import com.blog.infrastructure.services.contracts.PublicationService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@RestController
@RequestMapping("/api/v1/blog/publications")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "BLOG - PUBLICATIONS")
public class PublicationController {
    private final PublicationService publicationService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "servicio para alta de publicaciones al sistema",
            description = "servicio para alta de publicaciones al sistema")
    @Parameters({
            @Parameter(name = "title", description = "datos del usuario"),
            @Parameter(name = "description", description = "datos del usuario"),
            @Parameter(name = "userId", description = "datos del usuario"),
            @Parameter(name = "file", description = "archivo de publicacion")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<PublicationResponse> create(@RequestParam String title,
                                                      @RequestParam String description,
                                                      @RequestParam Long userId,
                                                      @RequestPart MultipartFile file){
        log.info("#### endpoint alta de publicaciones ####");
        var request = PublicationRequest.builder()
                .title(title)
                .description(description)
                .userId(userId)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.create(request, file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "servicio para consulta de publicaciones por id",
            description = "servicio para consulta de publicaciones por id si existe")
    @Parameter(name = "id", description = "id de la publicacion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<PublicationResponse> getById(@PathVariable Long id){
        log.info("#### endpoint consulta publicaciones por id ####");
        return ResponseEntity.ok(publicationService.getById(id));
    }

    @GetMapping("/title")
    @Operation(summary = "servicio para consulta de publicaciones por titulo",
            description = "servicio para publicaciones por titulo")
    @Parameters({
            @Parameter(name = "page", description = "numero de pagina"),
            @Parameter(name = "size", description = "cantidad de elementos"),
            @Parameter(name = "sortBy", description = "ordenamiento"),
            @Parameter(name = "title", description = "titulo de la publicacion")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<Page<PublicationResponse>> getByTitle(@RequestParam int page,
                                                                @RequestParam int size,
                                                                @RequestParam SortType sortType,
                                                                @RequestParam String title){
        log.info("#### endpoint consulta de publicaciones por nombre ####");
        var publications = publicationService.getByTitle(page, size, sortType, title);
        if (publications.getContent().isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(publications);
    }

    @GetMapping
    @Operation(summary = "servicio para consulta de publicaciones",
            description = "servicio para publicaciones")
    @Parameters({
            @Parameter(name = "page", description = "numero de pagina"),
            @Parameter(name = "size", description = "cantidad de elementos"),
            @Parameter(name = "sortBy", description = "ordenamiento")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<Page<PublicationResponse>> getAll(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam SortType sortType){
        log.info("#### endpoint consulta de publicaciones ####");
        var publications = publicationService.getAll(page, size, sortType);
        if (publications.getContent().isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(publications);
    }
}
