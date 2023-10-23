package com.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@Entity
@Table(name = "publications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 70)
    private String title;
    @Column(nullable = false)
    private String description;
    @CreationTimestamp
    private LocalDate createAt;
    @UpdateTimestamp
    private LocalDate updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
    @Column(nullable = false)
    private String document;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
               orphanRemoval = true, mappedBy = "publication")
    private Set<Comment> comments;
}
