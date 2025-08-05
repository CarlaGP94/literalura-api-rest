package com.alura.literalura_api_rest.domain.book;

import com.alura.literalura_api_rest.domain.author.Author;
import com.alura.literalura_api_rest.domain.author.IAuthorRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class IBookRepositoryTest {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IAuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        // Creamos un autor para asociarlo a los libros
        Author jkRowling = new Author();
        jkRowling.setCompleteName("J.K. Rowling");
        authorRepository.save(jkRowling);

        // Libros de prueba
        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Sorcerer's Stone");
        book1.setLanguage(Language.INGLES);
        book1.setAuthor(jkRowling);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Harry Potter y la piedra filosofal");
        book2.setLanguage(Language.ESPANOL);
        book2.setAuthor(jkRowling);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("The Hobbit");
        book3.setLanguage(Language.INGLES);
        bookRepository.save(book3);
    }

    // --- Test para findByTitleContainsIgnoreCase ---
    @Test
    @DisplayName("Cuando el título coincide parcialmente, se deben devolver los libros coincidentes")
    void findByTitleContainsIgnoreCase_scenery1() {
        // ARRANGE
        String userBookTitle = "harry potter";

        // ACT
        List<Book> result = bookRepository.findByTitleContainsIgnoreCase(userBookTitle);

        // ASSERT
        assertFalse(result.isEmpty(), "La lista no debe estar vacía");
        assertEquals(2, result.size(), "Debe encontrar los dos libros de Harry Potter");
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals("Harry Potter and the Sorcerer's Stone")));
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals("Harry Potter y la piedra filosofal")));
    }

    @Test
    @DisplayName("Cuando el título no coincide, se debe devolver una lista vacía")
    void findByTitleContainsIgnoreCase_scenery2() {
        // ARRANGE
        String userBookTitle = "Lord of the Rings";

        // ACT
        List<Book> result = bookRepository.findByTitleContainsIgnoreCase(userBookTitle);

        // ASSERT
        assertTrue(result.isEmpty(), "La lista debe estar vacía si no hay coincidencias");
    }

    // --- Test para findByTitleEqualsIgnoreCase ---
    @Test
    @DisplayName("Cuando el título coincide exactamente, debería devolver el libro")
    void findByTitleEqualsIgnoreCase_scenery1() {
        // ARRANGE
        String userBookTitle = "harry potter and the sorcerer's stone";

        // ACT
        Optional<Book> result = bookRepository.findByTitleEqualsIgnoreCase(userBookTitle);

        // ASSERT
        assertTrue(result.isPresent(), "El Optional debe contener el libro");
        assertEquals("Harry Potter and the Sorcerer's Stone", result.get().getTitle(), "El título debe ser el mismo");
    }

    @Test
    @DisplayName("Cuando el título no coincide exactamente, debe devolverse vacío opcional")
    void findByTitleEqualsIgnoreCase_scenery2() {
        // ARRANGE
        String userBookTitle = "Harry Potter"; // Coincide parcialmente, pero no exactamente

        // ACT
        Optional<Book> result = bookRepository.findByTitleEqualsIgnoreCase(userBookTitle);

        // ASSERT
        assertTrue(result.isEmpty(), "El Optional debe estar vacío si no hay coincidencia exacta");
    }

    // --- Test para findByLanguage ---
    @Test
    @DisplayName("Cuando el idioma es inglés, se deben devolver todos los libros en inglés.")
    void findByLanguage_scenery1() {
        // ARRANGE
        Pageable paginacion = PageRequest.of(0, 10);

        // ACT
        Page<Book> result = bookRepository.findByLanguage(paginacion, Language.INGLES);

        // ASSERT
        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements(), "Debe encontrar 2 libros en inglés");
        assertTrue(result.getContent().stream().allMatch(b -> b.getLanguage().equals(Language.INGLES)));
    }

    @Test
    @DisplayName("Cuando el idioma no existe, se debe devolver una página vacía")
    void findByLanguage_scenery2() {
        // ARRANGE
        Pageable paginacion = PageRequest.of(0, 10);

        // ACT
        Page<Book> result = bookRepository.findByLanguage(paginacion, Language.FRANCES);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}