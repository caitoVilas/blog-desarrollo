package com.blog.api.controllers;

import com.blog.api.models.responses.UserResponse;
import com.blog.infrastructure.services.contracts.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@RestController
@RequestMapping("/api/v1/blog/roles")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "BLOG - ROLES")
public class RoleController {
    private final RoleService roleService;

    @PutMapping("/role-admin/{id}")
    @Operation(summary = "servicio para asignar rol de Administrador (Solo Administradores)",
            description = "servicio para asignar rol de Administrador (Solo Administradores)")
    @Parameter(name = "id", description = "id del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "400", description = "bad reqest"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<UserResponse> setAdmin(@PathVariable Long id){
        log.info("#### endpoint setear rol AMIN a un usuario ####");
        return ResponseEntity.ok(roleService.changeRoleAdmin(id));
    }

    @PutMapping("/role-user/{id}")
    @Operation(summary = "servicio para asignar rol de Usuario (Solo Administradores)",
               description = "servicio para asignar rol de Usuario (Solo Administradores)")
    @Parameter(name = "id", description = "id del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "400", description = "bad reqest"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<UserResponse> setUser(@PathVariable Long id){
        log.info("#### endpoint setear rol USER a un usuario ####");
        return ResponseEntity.ok(roleService.changeRoleUser(id));
    }
}
