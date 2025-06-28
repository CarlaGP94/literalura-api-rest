package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.dto.AuthorDetail;
import com.alura.literalura_api_rest.repository.IAuthorRepository;
import com.alura.literalura_api_rest.service.IAuthorService;
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
    private IAuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorDetail>> showAllAuthors(@PageableDefault(size = 10) Pageable paginacion) {

        var allAuthors = authorService.showAllAuthors(paginacion);

        return ResponseEntity.ok(allAuthors);
    }

    @GetMapping("/active_in")
    public ResponseEntity<Page<AuthorDetail>> showBooksByLanguage(@PageableDefault(size = 10, sort = {"completeName"}) Pageable paginacion,
                                                                 @RequestParam String startYear,
                                                                   @RequestParam String endYear){

        var authors = authorService.showBooksByLanguage(paginacion, startYear, endYear);

        return ResponseEntity.ok(authors);
    }
}