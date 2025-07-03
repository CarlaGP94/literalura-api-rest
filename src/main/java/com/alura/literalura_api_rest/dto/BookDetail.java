package com.alura.literalura_api_rest.dto;

import com.alura.literalura_api_rest.model.Book;
import com.alura.literalura_api_rest.model.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDetail(Long id,
                         String title,
                         AuthorDetail author,
                         Language language,
                         Double downloadCount
) {

    public BookDetail(Book book) {
        this(book.getId(),
                book.getTitle(),
                new AuthorDetail(book.getAuthor()),
                book.getLanguage(),
                book.getDownloadCount()
        );
    }
}
