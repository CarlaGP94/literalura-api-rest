package com.alura.literalura_api_rest.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Optional;

public interface IBookRepository extends JpaRepository<Book,Long> {

    List<Book> findByTitleContainsIgnoreCase(String userBook);

    Optional<Book> findByTitleEqualsIgnoreCase(String userBook);

    @Override
    Page<Book> findAll(Pageable paginacion);

    Page<Book> findByLanguage(Pageable paginacion, Language language);
}
