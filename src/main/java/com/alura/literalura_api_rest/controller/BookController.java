package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.model.Book;
import com.alura.literalura_api_rest.dto.BookDetail;
import com.alura.literalura_api_rest.repository.IBookRepository;
import com.alura.literalura_api_rest.model.Language;
import com.alura.literalura_api_rest.service.IBookService;
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
    private IBookService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookDetail>> findBookByTitle(@RequestParam String booksTitle) {

        var bookDetail = bookService.findBookByTitle(booksTitle);

        return ResponseEntity.ok(bookDetail);
    }

    @GetMapping
    public ResponseEntity<Page<BookDetail>> showAllBooks(@PageableDefault(size = 10) Pageable paginacion) {

        var allBooks = bookService.showAllBooks(paginacion);

        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/language")
    public ResponseEntity<Page<BookDetail>> showBooksByLanguage(@PageableDefault(size = 10, sort = {"title"}) Pageable paginacion,
                                               @RequestParam Language language){

        var books = bookService.showBooksByLanguage(paginacion, language);

        return ResponseEntity.ok(books);
    }
}

