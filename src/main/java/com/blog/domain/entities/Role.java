package com.blog.domain.entities;

import com.blog.util.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author claudio.vilas
 * date 09/2023
 */

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private RoleName authority;

}
