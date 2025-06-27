package com.alura.literalura_api_rest.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IBookRepository extends JpaRepository<Book,Long> {

    List<Book> findByTitleContainsIgnoreCase(String userBook);
    Optional<Book> findByTitleEqualsIgnoreCase(String userBook);
}
