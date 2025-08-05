package com.alura.literalura_api_rest.domain.external.api_gutendex.record;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeneralDataGutendex(
        @JsonAlias("results") List<BookDataGutendex> booksList
) {
}
