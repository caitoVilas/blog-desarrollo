package com.blog.infrastructure.services.impl;

import com.blog.api.exceptions.customs.BadRequestException;
import com.blog.api.exceptions.customs.NotFoundException;
import com.blog.api.models.responses.UserResponse;
import com.blog.domain.entities.Role;
import com.blog.domain.repositories.RoleRepository;
import com.blog.domain.repositories.UserRepository;
import com.blog.infrastructure.services.contracts.RoleService;
import com.blog.util.constants.RoleConst;
import com.blog.util.constants.UserConstants;
import com.blog.util.enums.RoleName;
import com.blog.util.map.UserMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public UserResponse changeRoleAdmin(Long userId) {
        log.info("---> inicio servicio asignar rol AMIN a usuario");
        log.debug("---> uscando usurio con id {}...", userId);
        var user = userRepository.findById(userId).orElseThrow(()-> {
            log.error("ERROR : ".concat(UserConstants.U_ID_NOT_FOUND).concat(userId.toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(userId.toString()));
        });
        user.getAuthorities().forEach(rol ->{
            if (rol.getAuthority().equals(RoleName.ROLE_ADMIN)){
                log.error("ERROR: ".concat(RoleConst.R_IS_ADMIN));
                throw new BadRequestException(RoleConst.R_IS_ADMIN);
            }
        });
        var aut = user.getAuthorities();
        Role role = roleRepository.findByAuthority(RoleName.ROLE_ADMIN);
        aut.clear();
        aut.add(role);
        user.setAuthorities(aut);
        log.info("---> guardando cambio de rol...");
        var userModified = userRepository.save(user);
        log.info("---> finalzado servicio asignar rol ADMIN a usuario");
    return UserMap.mapToDto(userModified);
    }

    @Override
    public UserResponse changeRoleUser(Long userId) {
        log.info("---> inicio servicio asignar rol USER a usuario");
        log.debug("---> uscando usurio con id {}...", userId);
        var user = userRepository.findById(userId).orElseThrow(()-> {
            log.error("ERROR : ".concat(UserConstants.U_ID_NOT_FOUND).concat(userId.toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(userId.toString()));
        });
        user.getAuthorities().forEach(rol ->{
            if (rol.getAuthority().equals(RoleName.ROLE_USER)){
                log.error("ERROR: ".concat(RoleConst.R_IS_USER));
                throw new BadRequestException(RoleConst.R_IS_USER);
            }
        });
        var aut = user.getAuthorities();
        Role role = roleRepository.findByAuthority(RoleName.ROLE_USER);
        aut.clear();
        aut.add(role);
        user.setAuthorities(aut);
        log.info("---> guardando cambio de rol...");
        var userModified = userRepository.save(user);
        log.info("---> finalzado servicio asignar rol ADMIN a usuario");
        return UserMap.mapToDto(userModified);
    }
}
