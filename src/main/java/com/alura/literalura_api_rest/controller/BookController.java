package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.book.Book;
import com.alura.literalura_api_rest.book.BookDetail;
import com.alura.literalura_api_rest.book.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    IBookRepository bookRepository;

    @GetMapping("/search")
    private ResponseEntity<List<BookDetail>> findBookByTitle(@RequestParam String booksTitle) {

        List<Book> foundBook = bookRepository.findByTitleContainsIgnoreCase(booksTitle);

        List<BookDetail> bookDetail = foundBook.stream()
                .map(BookDetail::new)
                .toList();

        return ResponseEntity.ok(bookDetail);
    }
}

//    private void registeredBook() {
//        List<Book> savedBooks = bookRepository.findAll();
//        savedBooks.forEach(System.out::println);
//    }
//
//    private void registeredBookByLanguage() {
//        System.out.println("Ingrese el idioma para la búsqueda:\n" +
//                "\"en\" para libros en inglés." +
//                "\n\"es\" para libros en español." +
//                "\n\"fr\" para libros en francés.");
//
//        var userLanguage = keyboard.nextLine();
//
//        var userLanguageInput = Language.fromString(userLanguage);
//
//        List<Book> foundBookLanguage = bookRepository.registeredBookByLanguage(userLanguageInput);
//
//        System.out.println("Filtrando libros por idioma...\n");
//        foundBookLanguage.forEach(System.out::println);
//    }
//}

