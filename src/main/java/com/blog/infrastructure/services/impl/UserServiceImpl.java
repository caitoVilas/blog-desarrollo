package com.blog.infrastructure.services.impl;

import com.blog.api.exceptions.customs.BadRequestException;
import com.blog.api.exceptions.customs.NotFoundException;
import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.UserResponse;
import com.blog.domain.repositories.UserRepository;
import com.blog.infrastructure.services.contracts.UserService;
import com.blog.util.constants.UserConstants;
import com.blog.util.enums.SortType;
import com.blog.util.map.UserMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse getById(Long id) {
        log.info("---> inicio servicio buscar usuario por id");
        log.debug("---> buscando usuario con id {}...", id);
        var user = userRepository.findById(id).orElseThrow(()-> {
            log.error("ERROR : ".concat(UserConstants.U_ID_NOT_FOUND).concat(id.toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(id.toString()));
        });
        log.info("---> finalizado servicio buscar usuario por id");
        return UserMap.mapToDto(user);
    }

    @Override
    public Page<UserResponse> getAll(int page, int size, SortType sortType) {
        if (page < 0) page = 0;
        if (size < 0) size = 5;
        log.info("---> inicio servicio mostrar usuarios");
        PageRequest pr = null;
        switch (sortType){
            case UPPER ->
                pr = PageRequest.of(page, size, Sort.by("username").descending());
            case LOWER ->
                pr = PageRequest.of(page, size, Sort.by("username").ascending());
            case NONE ->
                pr = PageRequest.of(page, size);
        }
        log.info("---> buscando clientes...");
        log.info("---> finalizado servicio mostrar usuarios");
        return userRepository.findAll(pr).map(UserMap::mapToDto);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        log.info("---> inicio servicio actualizar usuario");
        log.debug("---> buscando usuario con id {}...", id);
        var user = userRepository.findById(id).orElseThrow(()-> {
            log.error("ERROR : ".concat(UserConstants.U_ID_NOT_FOUND).concat(id.toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(id.toString()));
        });
        if (!request.getUsername().isBlank()){
            user.setUsername(request.getUsername());
        }
        if (!request.getEmail().isBlank()){
            if (this.validateEmail(request.getEmail())){
                user.setEmail(request.getEmail());
            }
        }
        var userNew = userRepository.save(user);
        log.info("---> finalizado servicio actualizar usuario");
        return UserMap.mapToDto(userNew);
    }

    @Override
    public void delete(Long id) {
        log.info("---> inicio servicio eliminar usuario");
        log.debug("---> buscando usuario con id {}...", id);
        userRepository.findById(id).orElseThrow(()-> {
            log.error("ERROR : ".concat(UserConstants.U_ID_NOT_FOUND).concat(id.toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(id.toString()));
        });
        userRepository.deleteById(id);
    }

    private boolean validateEmail(String email){
        if (userRepository.existsByEmail(email)){
            log.error("ERROR: ".concat(UserConstants.U_EMAIL_EXISTS.concat(email)));
            throw new BadRequestException(UserConstants.U_EMAIL_EXISTS.concat(email));
        }
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            log.info("---> mail valido");
            return true;
        }else{
            log.error("ERROR: ".concat(UserConstants.U_EMAIL_NO_VALID));
            throw new BadRequestException(UserConstants.U_EMAIL_NO_VALID);
        }
    }
}
