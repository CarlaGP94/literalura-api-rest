package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.domain.book.BookDetail;
import com.alura.literalura_api_rest.domain.book.Language;
import com.alura.literalura_api_rest.domain.book.IBookService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<List<BookDetail>> findBookByTitle(@RequestParam @NotBlank String booksTitle) {

        var bookDetail = bookService.findBookByTitle(booksTitle);

        return ResponseEntity.ok(bookDetail);
    }

    @GetMapping
    public ResponseEntity<Page<BookDetail>> showAllBooks(
            @PageableDefault(size = 10) Pageable paginacion) {

        var allBooks = bookService.showAllBooks(paginacion);

        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/language")
    public ResponseEntity<Page<BookDetail>> showBooksByLanguage(@PageableDefault(size = 10, sort = {"title"}) Pageable paginacion,
                                               @RequestParam @NotNull Language language){

        var books = bookService.showBooksByLanguage(paginacion, language);

        return ResponseEntity.ok(books);
    }
}

