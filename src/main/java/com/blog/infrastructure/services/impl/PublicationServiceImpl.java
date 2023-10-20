package com.blog.infrastructure.services.impl;

import com.blog.api.exceptions.customs.BadRequestException;
import com.blog.api.exceptions.customs.NotFoundException;
import com.blog.api.models.requests.PublicationRequest;
import com.blog.api.models.responses.PublicationResponse;
import com.blog.domain.entities.Publication;
import com.blog.domain.repositories.PublicationRepository;
import com.blog.domain.repositories.UserRepository;
import com.blog.infrastructure.services.contracts.PublicationService;
import com.blog.util.constants.PublicationConst;
import com.blog.util.constants.UserConstants;
import com.blog.util.enums.SortType;
import com.blog.util.map.PublicationMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date 10/2023
 */

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;

    @Value("${publications.path}")
    private String documentPath;

    @Override
    public PublicationResponse create(PublicationRequest request, MultipartFile file) {
        log.info("---> inicio servicio creacion publicaciones");
        log.info("---> validando entradas...");
        this.validatePublication(request);
        var user = userRepository.findById(request.getUserId()).orElseThrow(()-> {
            log.error("ERROR : ".concat(UserConstants.U_ID_NOT_FOUND).concat(request.getUserId().toString()));
            return new NotFoundException(UserConstants.U_ID_NOT_FOUND.concat(request.getUserId().toString()));
        });
        if (file.isEmpty()){
            log.error("ERROR: ".concat(PublicationConst.P_FILE_NO_CONTENT));
            throw new BadRequestException(PublicationConst.P_FILE_NO_CONTENT);
        }
        var filename = this.saveFile(file);
        var publication = Publication.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .document(filename)
                .build();
        var publicationNew = publicationRepository.save(publication);
        return PublicationMap.mapToDto(publicationNew);
    }

    @Override
    public PublicationResponse getById(Long id) {
        log.info("---> inicio servicio buscar publicacion por id");
        log.debug("---> buscando publicacion con id {}...", id);
        var publication = publicationRepository.findById(id).orElseThrow(()->{
            log.error("ERROR : ".concat(PublicationConst.P_ID_NOT_FOUND).concat(id.toString()));
            return new NotFoundException(PublicationConst.P_ID_NOT_FOUND.concat(id.toString()));
        });
        return PublicationMap.mapToDto(publication);
    }

    @Override
    public Page<PublicationResponse> getByTitle(int page, int size, SortType sortType,
                                                String title) {
        log.info("---> inicio servicio buscar publicaciones por titulo");
        log.debug("---> buscando publicaciones con titulo {}...", title);
        if (page < 0) page = 0;
        if (size < 0) size = 1;
        PageRequest pr = null;
        switch (sortType){
            case UPPER ->
                pr = PageRequest.of(page, size, Sort.by("createAt").descending());
            case LOWER ->
                pr = PageRequest.of(page, size, Sort.by("createAt").ascending());
            case NONE ->
                pr = PageRequest.of(page, size);
        }
        log.info("---> finalizado servicio buscar publicaciones por titulo");
        return publicationRepository.findByTitleContaining(pr, title).map(PublicationMap::mapToDto);
    }

    @Override
    public Page<PublicationResponse> getAll(int page, int size, SortType sortType) {
        log.info("---> inicio servicio buscar publicaciones");
        log.debug("---> buscando publicaciones...");
        if (page < 0) page = 0;
        if (size > 0) size = 5;
        PageRequest pr = null;
        switch (sortType){
            case LOWER ->
                pr = PageRequest.of(page, size, Sort.by("createAt").ascending());
            case UPPER ->
                pr = PageRequest.of(page, size, Sort.by("createAt").descending());
            case NONE ->
                pr = PageRequest.of(page, size);
        }
        log.info("---> finalizado servicio buscar publicaciones");
        return publicationRepository.findAll(pr).map(PublicationMap::mapToDto);
    }


    private void validatePublication(PublicationRequest request){
        if (request.getTitle().isBlank()){
            log.error("ERROR: ".concat(PublicationConst.P_NO_TITLE));
            throw new BadRequestException(PublicationConst.P_NO_TITLE);
        }
        if (publicationRepository.existsByTitle(request.getTitle())){
            log.error("ERROR: ".concat(PublicationConst.P_TITLE_EXIXTS).concat(request.getTitle()));
            throw new BadRequestException(PublicationConst.P_TITLE_EXIXTS.concat(request.getTitle()));
        }
    }

    private String saveFile(MultipartFile file){
        try {
            byte[] fileBytes = file.getBytes();
            String filename = UUID.randomUUID().toString().concat(file.getOriginalFilename());
            Files.write(Path.of(documentPath + "\\" + filename), fileBytes);
            var rutaAbsoluta = new File(filename).getAbsolutePath();
            //return Path.of(documentPath) + "\\" + filename;
            return rutaAbsoluta;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
