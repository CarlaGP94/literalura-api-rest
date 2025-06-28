package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.author.AuthorDetail;
import com.alura.literalura_api_rest.author.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")

public class AuthorController {

    @Autowired
    private IAuthorRepository authorRepository;

    @GetMapping
    private ResponseEntity<Page<AuthorDetail>> showAllAuthors(@PageableDefault(size = 10) Pageable paginacion) {

        var allAuthors = authorRepository.findAll(paginacion)
                .map(AuthorDetail::new);

        return ResponseEntity.ok(allAuthors);
    }

    @GetMapping("/active_in")
    private ResponseEntity<Page<AuthorDetail>> showBooksByLanguage(@PageableDefault(size = 10, sort = {"completeName"}) Pageable paginacion,
                                                                 @RequestParam String startYear,
                                                                   @RequestParam String endYear){

        var authors = authorRepository.activeIn(paginacion, startYear, endYear)
                .map(AuthorDetail::new);

        return ResponseEntity.ok(authors);
    }
}