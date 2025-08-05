package com.alura.literalura_api_rest.domain.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService implements IAuthorService {

    @Autowired
    private IAuthorRepository authorRepository;

    @Override
    public Page<AuthorDetail> showAllAuthors(Pageable paginacion) {

        return authorRepository.findAll(paginacion)
                .map(AuthorDetail::new);
    }

    @Override
    public Page<AuthorDetail> showBooksByLanguage(Pageable paginacion, Integer startYear, Integer endYear){

        return authorRepository.activeIn(paginacion, startYear, endYear)
                .map(AuthorDetail::new);
    }
}
