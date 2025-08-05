package com.alura.literalura_api_rest.domain.book;

import com.alura.literalura_api_rest.domain.author.AuthorDetail;

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
