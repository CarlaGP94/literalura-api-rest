package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.book.Book;
import com.alura.literalura_api_rest.book.BookDetail;
import com.alura.literalura_api_rest.book.IBookRepository;
import com.alura.literalura_api_rest.book.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookRepository bookRepository;

    @GetMapping("/search")
    private ResponseEntity<List<BookDetail>> findBookByTitle(@RequestParam String booksTitle) {

        List<Book> foundBook = bookRepository.findByTitleContainsIgnoreCase(booksTitle);

        List<BookDetail> bookDetail = foundBook.stream()
                .map(BookDetail::new)
                .toList();

        return ResponseEntity.ok(bookDetail);
    }

    @GetMapping
    private ResponseEntity<Page<BookDetail>> showAllBooks(@PageableDefault(size = 10) Pageable paginacion) {

        var allBooks = bookRepository.findAll(paginacion)
                .map(BookDetail::new);

        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/language")
    private ResponseEntity<Page<BookDetail>> showBooksByLanguage(@PageableDefault(size = 10, sort = {"title"}) Pageable paginacion,
                                               @RequestParam Language language){

        var books = bookRepository.findByLanguage(paginacion, language)
                .map(BookDetail::new);

        return ResponseEntity.ok(books);
    }
}

