package com.blog.domain.repositories;

import com.blog.domain.entities.Comment;
import com.blog.domain.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> getByPublication(PageRequest pr, Publication publication);
}
