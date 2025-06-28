package com.alura.literalura_api_rest.service;

import com.alura.literalura_api_rest.dto.BookDetail;
import com.alura.literalura_api_rest.model.Book;
import com.alura.literalura_api_rest.model.Language;
import com.alura.literalura_api_rest.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
