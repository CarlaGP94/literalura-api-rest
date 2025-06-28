package com.alura.literalura_api_rest.dto;

import com.alura.literalura_api_rest.model.Book;
import com.alura.literalura_api_rest.model.Language;

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
