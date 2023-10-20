package com.blog.domain.repositories;

import com.blog.domain.entities.Publication;
import com.blog.util.enums.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    boolean existsByTitle(String title);
    Page<Publication> findByTitleContaining(PageRequest pr, String title);
}
