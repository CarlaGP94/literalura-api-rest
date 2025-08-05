package com.alura.literalura_api_rest.domain.author;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class IAuthorRepositoryTest {

    @Autowired
    private IAuthorRepository authorRepository;

    @BeforeEach
        // Este metodo se ejecuta antes de cada test para limpiar y preparar
    void setUp() {
        authorRepository.deleteAll();

        // 1. Autor vivo y activo en el rango
        Author author1 = new Author();
        author1.setCompleteName("George Orwell");
        author1.setBirthYear(1903);
        author1.setDeathYear(null);
        authorRepository.save(author1);

        // 2. Autor que murió dentro del rango
        Author author2 = new Author();
        author2.setCompleteName("Virginia Woolf");
        author2.setBirthYear(1882);
        author2.setDeathYear(1941);
        authorRepository.save(author2);

        // 3. Autor que no está activo en el rango (murió antes)
        Author author3 = new Author();
        author3.setCompleteName("Jane Austen");
        author3.setBirthYear(1775);
        author3.setDeathYear(1817);
        authorRepository.save(author3);

        // 4. Autor que nace y muere fuera del rango
        Author author4 = new Author();
        author4.setCompleteName("Stephen King");
        author4.setBirthYear(1951);
        author4.setDeathYear(null); // Vivo
        authorRepository.save(author4);
    }

    @Test
    @DisplayName("Cuando el autor existe, debe devolverse opcional con el autor")
    void findByCompleteName_scenery1() {
        // ARRANGE
        String existingAuthorName = "Jane Austen";

        // ACT
        Optional<Author> result = authorRepository.findByCompleteName(existingAuthorName);

        // ASSERT
        assertTrue(result.isPresent(), "El Optional no debe estar vacío");
        assertEquals(existingAuthorName, result.get().getCompleteName(), "El nombre del autor debe coincidir");
    }

    @Test
    @DisplayName("Cuando el autor no existe, debe devolverse vacío opcional")
    void findByCompleteName_scenery2() {
        // ARRANGE
        String nonExistentAuthorName = "Stephenie Meyer";

        // ACT
        Optional<Author> result = authorRepository.findByCompleteName(nonExistentAuthorName);

        // ASSERT
        assertTrue(result.isEmpty(), "El Optional debe estar vacío para un autor inexistente");
    }

    @Test
    @DisplayName("Debe devolver los autores correctos para el rango de años dado.")
    void activeIn_scenery1() {
        // ARRANGE
        Integer startYear = 1900;
        Integer endYear = 1950;
        Pageable pageable = PageRequest.of(0, 10);

        // ACT
        Page<Author> resultPage = authorRepository.activeIn(pageable, startYear, endYear);

        // ASSERT
        assertFalse(resultPage.isEmpty());
        assertEquals(2, resultPage.getTotalElements(), "Debería encontrar 2 autores activos en el rango");

        List<Author> authors = resultPage.getContent();
        assertTrue(authors.stream().anyMatch(a -> a.getCompleteName().equals("George Orwell")));
        assertTrue(authors.stream().anyMatch(a -> a.getCompleteName().equals("Virginia Woolf")));
        assertFalse(authors.stream().anyMatch(a -> a.getCompleteName().equals("Jane Austen")));
        assertFalse(authors.stream().anyMatch(a -> a.getCompleteName().equals("Stephen King")));
    }

    @Test
    @DisplayName("Cuando no hay autores que coincidan, debe devolver una página vacía.")
    void activeIn_scenery2() {
        // ARRANGE
        Integer startYear = 1850;
        Integer endYear = 1880;
        Pageable pageable = PageRequest.of(0, 10);

        // ACT
        Page<Author> resultPage = authorRepository.activeIn(pageable, startYear, endYear);

        // ASSERT
        assertTrue(resultPage.isEmpty(), "La lista debería estar vacía, no hay autores en ese rango");
    }
}