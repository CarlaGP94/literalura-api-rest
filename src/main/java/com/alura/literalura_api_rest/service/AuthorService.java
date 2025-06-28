package com.alura.literalura_api_rest.service;

import com.alura.literalura_api_rest.dto.AuthorDetail;
import com.alura.literalura_api_rest.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Service
public class AuthorService implements IAuthorService{

    @Autowired
    private IAuthorRepository authorRepository;

    @Override
    public Page<AuthorDetail> showAllAuthors(Pageable paginacion) {

        return authorRepository.findAll(paginacion)
                .map(AuthorDetail::new);
    }

    @Override
    public Page<AuthorDetail> showBooksByLanguage(Pageable paginacion, String startYear, String endYear){

        return authorRepository.activeIn(paginacion, startYear, endYear)
                .map(AuthorDetail::new);
    }
}
