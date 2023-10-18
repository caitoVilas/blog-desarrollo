package com.blog.domain.repositories;

import com.blog.domain.entities.Role;
import com.blog.util.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author claudio.vilas
 * date 09/2023
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(RoleName authority);
}
