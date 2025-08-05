package com.alura.literalura_api_rest.infra.configuration;

import com.alura.literalura_api_rest.domain.author.Author;
import com.alura.literalura_api_rest.domain.external.api_gutendex.record.AuthorDataGutendex;
import com.alura.literalura_api_rest.domain.author.IAuthorRepository;
import com.alura.literalura_api_rest.domain.book.Book;
import com.alura.literalura_api_rest.domain.external.api_gutendex.record.BookDataGutendex;
import com.alura.literalura_api_rest.domain.book.IBookRepository;
import com.alura.literalura_api_rest.domain.external.api_gutendex.GutendexClient;
import com.alura.literalura_api_rest.domain.external.api_gutendex.GutendexConverter;
import com.alura.literalura_api_rest.domain.external.api_gutendex.record.GeneralDataGutendex;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {
    private GutendexClient consumingAPI = new GutendexClient();
    private GutendexConverter converter = new GutendexConverter();
    private final String URL_BASE = "https://gutendex.com/books/?page=";
    private final Integer numPage = 50;
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

    public Map<String, Author> saveAuthor(List<BookDataGutendex> bookDataGutendexList) {
        Map<String, Author> authorFound = new HashMap<>();

        for (BookDataGutendex bookAPI : bookDataGutendexList) {
            if (bookAPI.authorList() != null && !bookAPI.authorList().isEmpty()) {
                AuthorDataGutendex authorAPI = bookAPI.authorList().get(0);

                // Verifica si ya existe en la base de datos o hay que agregarlo.
                Optional<Author> existingAuthor = authorRepository.findByCompleteName(authorAPI.completeName());

                if (existingAuthor.isPresent()) {
                    authorFound.put(existingAuthor.get().getCompleteName(),existingAuthor.get());
                } else {
                    Author otherAuthor = new Author(authorAPI);
                    authorRepository.save(otherAuthor); // Guarda al author
                    authorFound.put(otherAuthor.getCompleteName(),otherAuthor);
                }
            }
        }
        return authorFound;
    }

    public List<Book> saveBook(List<BookDataGutendex> bookDataGutendexList, Map<String, Author> authorMap){
        var bookList = new ArrayList<Book>();

        if (bookDataGutendexList != null && !bookDataGutendexList.isEmpty() &&
                authorMap != null && !authorMap.isEmpty()) {

            for (BookDataGutendex bookAPI : bookDataGutendexList) {

                Optional<Book> existingBook = bookRepository.findByTitleEqualsIgnoreCase(bookAPI.title());

                if (existingBook.isPresent()) {
                    bookList.add(existingBook.get()); // Si existe, toma los datos.
                } else {
                    Book otherBook = new Book(bookAPI);

                    if(bookAPI.authorList() != null && !bookAPI.authorList().isEmpty()){
                        var name = bookAPI.authorList().get(0).completeName(); // Toma el nombre del autor.
                        var nameAuthor = authorMap.get(name); // Crea al autor.
                        otherBook.setAuthor(nameAuthor); // Lo asocia al libro.
                        bookRepository.save(otherBook); // Guarda el nuevo libro.
                        bookList.add(otherBook);
                    }



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

