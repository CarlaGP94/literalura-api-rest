package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.domain.author.AuthorDetail;
import com.alura.literalura_api_rest.domain.author.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private JacksonTester<Page<AuthorDetail>> jsonPageAuthorDetail;

    @Autowired
    private JacksonTester<AuthorDetail> jsonAuthorDetail;

    @Test
    @DisplayName("Debe retornar una lista de autores cuando se accede a 'showAllAuthors'")
    @WithMockUser
    void showAllAuthors_scenery1() throws Exception {
        // ARRANGE
        AuthorDetail mockAuthorDetail = new AuthorDetail(1L, "Test Author", 1900, null);
        Page<AuthorDetail> mockPage = new PageImpl<>(Collections.singletonList(mockAuthorDetail));

        when(authorService.showAllAuthors(any(Pageable.class))).thenReturn(mockPage);

        // ACT & ASSERT
        mockMvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonPageAuthorDetail.write(mockPage).getJson()));
    }

    @Test
    @DisplayName("Debe retornar una lista de autores activos en un período de años específico")
    @WithMockUser
    void showBooksByActiveIn_scenery1() throws Exception {
        // ARRANGE
        int startYear = 1900;
        int endYear = 1950;

        AuthorDetail mockAuthorDetail = new AuthorDetail(1L, "Test Author", 1880, 1960);
        Page<AuthorDetail> mockPage = new PageImpl<>(Collections.singletonList(mockAuthorDetail));

        when(authorService.showAuthorsByActiveIn(any(Pageable.class), anyInt(), anyInt()))
                .thenReturn(mockPage);

        // ACT & ASSERT
        mockMvc.perform(get("/author/active_in")
                        .param("startYear", String.valueOf(startYear))
                        .param("endYear", String.valueOf(endYear)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonPageAuthorDetail.write(mockPage).getJson()));
    }
}