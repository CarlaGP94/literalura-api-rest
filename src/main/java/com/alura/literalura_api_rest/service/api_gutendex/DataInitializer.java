package com.alura.literalura_api_rest.service.api_gutendex;

import com.alura.literalura_api_rest.author.Author;
import com.alura.literalura_api_rest.author.AuthorDataGutendex;
import com.alura.literalura_api_rest.author.IAuthorRepository;
import com.alura.literalura_api_rest.book.Book;
import com.alura.literalura_api_rest.book.BookDataGutendex;
import com.alura.literalura_api_rest.book.IBookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {
    private APIConsumption consumingAPI = new APIConsumption();
    private ConvertsData converter = new ConvertsData();
    private final String URL_BASE = "https://gutendex.com/books/?page=";
    private final Integer numPage = 10;
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IAuthorRepository authorRepository;

    // Métodos para el consumo de la API.
    public List<GeneralDataGutendex> ConsumingAPI(String urlBase, Integer numPage) {
        List<GeneralDataGutendex> data = new ArrayList<>();

        for (int i = 1; i <= numPage ; i++) {
            var json = consumingAPI.getData(urlBase + i);
            var dataAPI = converter.getData(json, GeneralDataGutendex.class);
            data.add(dataAPI);
        }

        return data;
    }

    public List<BookDataGutendex> resultOfConsumingAPI (List<GeneralDataGutendex> data){
        List<BookDataGutendex> result = new ArrayList<>();

        for(GeneralDataGutendex dataOk : data){

            // Siempre verifica primero que las listas no estén vacías o null.
            if (dataOk.booksList() != null && !dataOk.booksList().isEmpty()) {
                result.addAll(dataOk.booksList());
            }
        }
        return result;
    }

    public List<Author> saveAuthor(List<BookDataGutendex> bookDataGutendexList) {
        var authorFound = new ArrayList<Author>();

        for (BookDataGutendex bookAPI : bookDataGutendexList) {
            if (bookAPI.authorList() != null && !bookAPI.authorList().isEmpty()) {
                AuthorDataGutendex authorAPI = bookAPI.authorList().get(0);

                // Verifica si ya existe en la base de datos o hay que agregarlo.
                Optional<Author> existingAuthor = authorRepository.findByCompleteName(authorAPI.completeName());

                if (existingAuthor.isPresent()) {
                    authorFound.add(existingAuthor.get());
                } else {
                    Author otherAuthor = new Author(authorAPI);
                    authorRepository.save(otherAuthor); // Guarda al author
                    authorFound.add(otherAuthor);
                }
            }
        }
        return authorFound;
    }

    public List<Book> saveBook(List<BookDataGutendex> bookDataGutendexList, List<Author> authorList){
        var bookList = new ArrayList<Book>();

        if (bookDataGutendexList != null && !bookDataGutendexList.isEmpty() &&
                authorList != null && !authorList.isEmpty()) {

            for (BookDataGutendex bookAPI : bookDataGutendexList) {

                Optional<Book> existingBook = bookRepository.findByTitleEqualsIgnoreCase(bookAPI.title());

                if (existingBook.isPresent()) {
                    bookList.add(existingBook.get()); // Si existe, toma los datos.
                } else {
                    Book otherBook = new Book(bookAPI);
                    otherBook.setAuthor(authorList.get(0));
                    bookRepository.save(otherBook); // Sino, guarda el nuevo libro.
                    bookList.add(otherBook);
                }
            }
        }

        return bookList;
    }

    // La base de datos contendrá los libros de las primeras 10 páginas de la API.
    public void createDataBase(){
        var data = ConsumingAPI(URL_BASE,numPage);
        var resultAPI = resultOfConsumingAPI(data);
        var authors = saveAuthor(resultAPI);
        var books = saveBook(resultAPI,authors);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createDataBase();
    }
}

