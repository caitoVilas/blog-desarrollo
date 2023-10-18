package com.blog.api.controllers;

import com.blog.api.models.requests.LoginRequest;
import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.LoginResponse;
import com.blog.api.models.responses.UserResponse;
import com.blog.infrastructure.services.contracts.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author claudio.vilas
 * date 09/2023
 */

@RestController
@RequestMapping("/api/v1/blog/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "BLOG - AUTH")
public class AuthController {
    private final LoginService loginService;


    @PostMapping("/login")
    @Operation(summary = "servicio para ingreso al sistema",
            description = "servicio para ingreso al sistema")
    @Parameter(name = "request", description = "datos del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "401", description = "unauthorized"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        log.info("#### endpoint de login ####");
        return ResponseEntity.ok(loginService.login(request));
    }
}
