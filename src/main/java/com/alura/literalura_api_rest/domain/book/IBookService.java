package com.alura.literalura_api_rest.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    List<BookDetail> findBookByTitle(String title);
    Page<BookDetail> showAllBooks(Pageable paginacion);
    Page<BookDetail> showBooksByLanguage(Pageable paginacion, Language language);
}
