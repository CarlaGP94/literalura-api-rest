package com.alura.literalura_api_rest.book;

import com.alura.literalura_api_rest.author.Author;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    private Author author;
    @Enumerated(EnumType.STRING) // atributo del tipo Enum
    private Language language;
    private Double downloadCount;
}
