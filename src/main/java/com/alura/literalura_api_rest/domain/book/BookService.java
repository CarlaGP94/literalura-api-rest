package com.alura.literalura_api_rest.domain.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookService implements IBookService {
    @Autowired
    private IBookRepository bookRepository;

    @Override
    public List<BookDetail> findBookByTitle(String booksTitle) {

        return bookRepository.findByTitleContainsIgnoreCase(booksTitle)
                .stream()
                .map(BookDetail::new)
                .toList();
    }

    @Override
    public Page<BookDetail> showAllBooks(Pageable paginacion) {

        return bookRepository.findAll(paginacion)
                .map(BookDetail::new);
    }

    @Override
    public Page<BookDetail> showBooksByLanguage(Pageable paginacion, Language language){

        return bookRepository.findByLanguage(paginacion, language)
                .map(BookDetail::new);
    }
}
