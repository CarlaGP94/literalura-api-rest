package com.alura.literalura_api_rest.author;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByCompleteName(String authorName);
}
