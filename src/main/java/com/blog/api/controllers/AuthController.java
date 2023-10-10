package com.blog.api.controllers;

import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.UserResponse;
import com.blog.infrastructure.services.contracts.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request){
        log.info("#### endpoint alta de usuario ####");
        return ResponseEntity.ok(loginService.create(request));
    }
}
