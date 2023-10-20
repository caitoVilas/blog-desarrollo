package com.blog.api.controllers;

import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.UserResponse;
import com.blog.infrastructure.services.contracts.LoginService;
import com.blog.infrastructure.services.contracts.UserService;
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
 * date 09/2023
 */

@RestController
@RequestMapping("/api/v1/blog/users")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "BLOG - USERS")
public class UserController {
    private final LoginService loginService;
    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "servicio para alta de usuarios al sistema",
            description = "servicio para alta de usuarios al sistema")
    @Parameter(name = "request", description = "datos del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request){
        log.info("#### endpoint alta de usuario ####");
        return ResponseEntity.status(HttpStatus.CREATED).body(loginService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "servicio para consulta de usuarios por id",
            description = "servicio para consulta de usuarios por id si existe")
    @Parameter(name = "id", description = "id del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<UserResponse> getById(@PathVariable Long id){
        log.info("#### endpoint consulta usuario por id ####");
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    @Operation(summary = "servicio para consulta de usuarios",
            description = "servicio para consulta de usuarios")
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
    public ResponseEntity<Page<UserResponse>> getAll(@RequestParam int page,
                                               @RequestParam int size,
                                               @RequestParam SortType sortType){
        log.info("#### endpoint consulta de usuarios ####");
        var users = userService.getAll(page, size, sortType);
        if (users.getContent().isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}")
    @Operation(summary = "servicio para actualizacion de usuarios",
            description = "servicio para actualizacion de usuarios")
    @Parameters({
            @Parameter(name = "id", description = "id del usuario"),
            @Parameter(name = "request", description = "datos del usuario")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "400", description = "bad reqest"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody UserRequest request){
        log.info("#### endpoint actualizacion de usuario ####");
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "servicio para baja de usuarios por id",
            description = "servicio para baja de usuarios por id si existe")
    @Parameter(name = "id", description = "id del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("#### endpoint baja de usuario por id ####");
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
