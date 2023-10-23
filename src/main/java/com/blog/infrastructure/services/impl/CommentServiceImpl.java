package com.blog.infrastructure.services.impl;

import com.blog.api.exceptions.customs.BadRequestException;
import com.blog.api.exceptions.customs.NotFoundException;
import com.blog.api.models.requests.CommentRequest;
import com.blog.api.models.responses.CommentResponse;
import com.blog.domain.entities.Comment;
import com.blog.domain.repositories.CommentRepository;
import com.blog.domain.repositories.PublicationRepository;
import com.blog.domain.repositories.UserRepository;
import com.blog.infrastructure.services.contracts.CommentService;
import com.blog.util.constants.CommentConst;
import com.blog.util.constants.PublicationConst;
import com.blog.util.constants.UserConstants;
import com.blog.util.enums.SortType;
import com.blog.util.map.CommentMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;


    @Override
    public CommentResponse create(CommentRequest request) {
        log.info("---> inicio serviocio crear comentarios");
        log.info("---> validando entradas...");
        this.validateComment(request);
        log.debug("--> buscando publicacion con id {}...", request.getPublicationId());
        var publication = publicationRepository.findById(request.getPublicationId())
                .orElseThrow(()-> {
                    log.error("ERROR: ".concat(PublicationConst.P_ID_NOT_FOUND)
                            .concat(request.getPublicationId().toString()));
                    return new NotFoundException(PublicationConst.P_ID_NOT_FOUND.concat(request
                            .getPublicationId().toString()));
                });
        log.debug("---> buscando usuario con id {}...", request.getUserId());
        var user = userRepository.findById(request.getUserId()).orElseThrow(()-> {
            log.error("ERROR: ".concat(UserConstants.U_ID_NOT_FOUND).concat(request.getUserId().toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(request.getUserId().toString()));
        });
        var comment = Comment.builder()
                .publication(publication)
                .user(user)
                .comment(request.getComment())
                .build();
        log.debug("---> guardando comentario...");
        var commentNew = commentRepository.save(comment);
        log.info("---> finalizado servicio crear comentarios");
        return CommentMap.mapToDto(commentNew);
    }

    @Override
    public CommentResponse getById(Long id) {
        log.info("---> inicio servicio buscar comentario por id");
        log.debug("---> buscando comentario con id {}...", id);
        var comment = commentRepository.findById(id).orElseThrow(()-> {
            log.error("ERROR: ".concat(CommentConst.C_ID_NOT_FOUND).concat(id.toString()));
            return new NotFoundException(CommentConst.C_ID_NOT_FOUND.concat(id.toString()));
        });
        log.info("finalizado servicio buscar comentario por id");
        return CommentMap.mapToDto(comment);
    }

    @Override
    public Page<CommentResponse> getByPublication(int page, int size, SortType sortType, Long publicationId) {
        log.info("---> inicio servicio buscar comentarios por publicacion");
        if (page < 0) page = 0;
        if (size < 0) size = 5;
        var publication = publicationRepository.findById(publicationId).orElseThrow(()-> {
            log.error("ERROR: ".concat(PublicationConst.P_ID_NOT_FOUND)
                    .concat(publicationId.toString()));
            return new NotFoundException(PublicationConst.P_ID_NOT_FOUND.concat(publicationId.toString()));
        });
        PageRequest pr = null;
        switch (sortType){
            case UPPER ->
                pr = PageRequest.of(page, size, Sort.by("createAt").descending());
            case LOWER ->
                pr = PageRequest.of(page, size, Sort.by("createAt").ascending());
            case NONE ->
                pr = PageRequest.of(page, size);
        }
        log.debug("---> buscando comentarios para publicacion id {}...", publicationId);
        log.info("---> finalizado servicio buscar comentarios por publicacion");
        return commentRepository.getByPublication(pr, publication).map(CommentMap::mapToDto);
    }

    @Override
    public void delete(Long id) {
        log.info("---> inicio servicio eliminar comentario por id");
        log.debug("---> buscando comentario con id {}", id);
        commentRepository.findById(id).orElseThrow(()-> {
            log.error("ERROR: ".concat(CommentConst.C_ID_NOT_FOUND).concat(id.toString()));
            return new NotFoundException(CommentConst.C_ID_NOT_FOUND.concat(id.toString()));
        });
        log.debug("---> eliminando comentario con id {}", id);
        commentRepository.deleteById(id);
        log.info("--> finalizado servicio eliminar comentario por id");
    }

    private void validateComment(CommentRequest request){
        if (request.getComment().isBlank()){
            log.error("ERROR: ".concat(CommentConst.C_NO_COMMENT));
            throw new BadRequestException(CommentConst.C_NO_COMMENT);
        }
    }
}
