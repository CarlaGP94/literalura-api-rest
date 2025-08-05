package com.alura.literalura_api_rest.domain.author;

import com.alura.literalura_api_rest.domain.book.Book;
import com.alura.literalura_api_rest.domain.external.api_gutendex.record.AuthorDataGutendex;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Author")
@Table(name = "author")

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, columnDefinition = "TEXT")
    private String completeName;
    private Integer birthYear;
    private Integer deathYear;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> booksList = new ArrayList<>();

    public Author(AuthorDataGutendex authorData) {
        this.completeName = authorData.completeName();
        this.birthYear = authorData.birthYear();
        this.deathYear = authorData.deathYear();
    }
}
