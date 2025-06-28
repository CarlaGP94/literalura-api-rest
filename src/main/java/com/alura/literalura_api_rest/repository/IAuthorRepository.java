package com.alura.literalura_api_rest.repository;

import com.alura.literalura_api_rest.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IAuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByCompleteName(String authorName);

    @Override
    Page<Author> findAll(Pageable paginacion);

    @Query("SELECT a FROM Author a " +
            "WHERE a.birthYear IS NOT NULL AND a.birthYear <= :endYear " + // Nacido antes o durante el año final.
            "AND (a.deathYear IS NULL OR a.deathYear >= :startYear)") // Sigue vivo, o murió después o durante el año inicial.
    Page<Author> activeIn(Pageable paginacion, String startYear, String endYear);

}
