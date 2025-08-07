package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.domain.author.AuthorDetail;
import com.alura.literalura_api_rest.domain.book.BookDetail;
import com.alura.literalura_api_rest.domain.book.BookService;
import com.alura.literalura_api_rest.domain.book.Language;
import com.alura.literalura_api_rest.infra.security.SecurityConfigurations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private JacksonTester<BookDetail> jsonBookDetail;

    // En este caso, el JSON de la página es más complejo, por lo que JacksonTester para Page es útil
    @Autowired
    private JacksonTester<Page<BookDetail>> jsonPageBookDetail;

    @Test
    @DisplayName("Debe retornar una lista de libros cuando se accede a 'showAllBooks'")
    @WithMockUser
    void showAllBooks_scenery1() throws Exception {
        // ARRANGE
        AuthorDetail mockAuthorDetail = new AuthorDetail(1L, "Test Author", 1900, null);
        BookDetail mockBookDetail = new BookDetail(1L, "Test Title", mockAuthorDetail, Language.ESPANOL, 100.1);
        Page<BookDetail> mockPage = new PageImpl<>(Collections.singletonList(mockBookDetail));

        when(bookService.showAllBooks(any(Pageable.class))).thenReturn(mockPage);

        // ACT & ASSERT
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonPageBookDetail.write(mockPage).getJson()));
    }

    @Test
    @DisplayName("Debe retornar un libro cuando se busca por título y se encuentra")
    @WithMockUser
    void findBookByTitle_scenery1() throws Exception {
        // ARRANGE
        String bookTitle = "Test Book Title";
        AuthorDetail mockAuthorDetail = new AuthorDetail(2L, "Author", 1950, 2020);
        BookDetail mockBookDetail = new BookDetail(2L, bookTitle, mockAuthorDetail, Language.INGLES, 50.1);
        List<BookDetail> mockList = Collections.singletonList(mockBookDetail);

        when(bookService.findBookByTitle(any())).thenReturn(mockList);

        // ACT & ASSERT
        mockMvc.perform(get("/book/search")
                        .param("booksTitle", bookTitle))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + jsonBookDetail.write(mockBookDetail).getJson() + "]"));
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no se encuentra el libro por título")
    @WithMockUser
    void findBookByTitle_scenery2() throws Exception {
        // ARRANGE
        String bookTitle = "Non-existent Book";
        when(bookService.findBookByTitle(anyString())).thenReturn(Collections.emptyList());

        // ACT & ASSERT
        mockMvc.perform(get("/book/search")
                        .param("booksTitle", bookTitle))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    @DisplayName("Debe retornar una lista de libros por idioma")
    @WithMockUser
    void showBooksByLanguage_scenery1() throws Exception {
        // ARRANGE
        String languageCode = Language.ESPANOL.toString();
        AuthorDetail mockAuthorDetail = new AuthorDetail(3L, "Autor", 1980, null);
        BookDetail mockBookDetail = new BookDetail(3L, "Spanish Book", mockAuthorDetail, Language.ESPANOL, 20.1);
        Page<BookDetail> mockPage = new PageImpl<>(Collections.singletonList(mockBookDetail));

        // Corregimos el mock para que coincida con la firma del servicio:
        when(bookService.showBooksByLanguage(any(Pageable.class), any(Language.class)))
                .thenReturn(mockPage);

        // ACT & ASSERT
        mockMvc.perform(get("/book/language")
                        .param("language", languageCode)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonPageBookDetail.write(mockPage).getJson()));
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no hay libros para un idioma")
    @WithMockUser
    void showBooksByLanguage_scenery2() throws Exception {
        // ARRANGE
        String unknownLanguage = Language.UNKNOWN.toString();
        Page<BookDetail> mockPage = Page.empty();

        // Corregimos el mock para que coincida con la firma del servicio:
        when(bookService.showBooksByLanguage(any(Pageable.class), any(Language.class)))
                .thenReturn(mockPage);

        // ACT & ASSERT
        mockMvc.perform(get("/book/language")
                        .param("language", unknownLanguage))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonPageBookDetail.write(mockPage).getJson()));
    }
}