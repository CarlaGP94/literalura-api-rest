package com.alura.literalura_api_rest.service.api_gutendex;

import com.alura.literalura_api_rest.book.BookDataGutendex;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeneralDataGutendex(
        @JsonAlias("results") List<BookDataGutendex> booksList
) {
}
