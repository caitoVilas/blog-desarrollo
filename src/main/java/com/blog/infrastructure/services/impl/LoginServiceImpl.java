package com.blog.infrastructure.services.impl;

import com.blog.api.exceptions.customs.BadRequestException;
import com.blog.api.models.requests.LoginRequest;
import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.LoginResponse;
import com.blog.api.models.responses.UserResponse;
import com.blog.components.JwtProvider;
import com.blog.domain.entities.ApplicationUser;
import com.blog.domain.entities.Role;
import com.blog.domain.repositories.RoleRepository;
import com.blog.domain.repositories.UserRepository;
import com.blog.infrastructure.services.contracts.LoginService;
import com.blog.util.constants.UserConstants;
import com.blog.util.enums.RoleName;
import com.blog.util.map.UserMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author claudio.vilas
 * date 09/2023
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse create(UserRequest request) {
        log.info("---> inicio servicio alta de usuario");
        log.info("---> validando entradas...");
        this.validateUser(request);
        var role = roleRepository.findByAuthority(RoleName.ROLE_USER);
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);
        var user = ApplicationUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .authorities(authorities)
                .build();
        var newUser = userRepository.save(user);
        log.info("---> finalizado servicio alta de usuario");
        return UserMap.mapToDto(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("---> inicio servicio login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        var user = userRepository.findByUsername(request.getUsername()).get();
        log.info("---> login OK");
        return LoginResponse.builder()
                .jwt(token)
                .user(UserMap.mapToDto(user))
                .build();
    }

    private void validateUser(UserRequest request){
        if (request.getUsername().isBlank()){
            log.error("ERROR: ".concat(UserConstants.U_NO_NAME));
            throw new BadRequestException(UserConstants.U_NO_NAME);
        }
        if (userRepository.existsByUsername(request.getUsername())){
            log.error("ERROR: ".concat(UserConstants.U_NAME_EXISTS.concat(request.getUsername())));
            throw new BadRequestException(UserConstants.U_NAME_EXISTS.concat(request.getUsername()));
        }
        if (request.getPassword().isBlank()){
            log.error("ERROR: ".concat(UserConstants.U_NO_PASS));
            throw new BadRequestException(UserConstants.U_NO_PASS);
        }
        if (request.getEmail().isBlank()){
            log.error("ERROR: ".concat(UserConstants.U_EMAIL_EMPTY));
            throw new BadRequestException(UserConstants.U_EMAIL_EMPTY);
        }
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(request.getEmail());
        if (matcher.find()) {
            log.info("---> mail valido");
        }else{
            log.error("ERROR: ".concat(UserConstants.U_EMAIL_NO_VALID));
            throw new BadRequestException(UserConstants.U_EMAIL_NO_VALID);
        }
    }
}
