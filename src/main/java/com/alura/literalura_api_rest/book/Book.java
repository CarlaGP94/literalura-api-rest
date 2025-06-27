package com.alura.literalura_api_rest.book;

import com.alura.literalura_api_rest.author.Author;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Book")
@Table(name = "books")

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, columnDefinition = "TEXT") // Acepta longitudes largas de caracteres -> SQL: ALTER TABLE books ALTER COLUMN title TYPE TEXT;
    private String title;
    @Setter
    @ManyToOne
    private Author author;
    @Enumerated(EnumType.STRING) // atributo del tipo Enum
    private Language language;
    private Double downloadCount;

    public Book(BookDataGutendex bookAPI) {
        this.title = bookAPI.title();
        this.language = Language.fromString(bookAPI.languagesList().get(0).trim());
        this.downloadCount = bookAPI.downloadCount();
    }

}
