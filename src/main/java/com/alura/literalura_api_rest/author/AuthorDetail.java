package com.alura.literalura_api_rest.author;

public record AuthorDetail(
                            Long id,
                           String completeName,
                           Integer birthYear,
                           Integer deathYear
) {
    public AuthorDetail(Author author){
        this(author.getId(),
                author.getCompleteName(),
                author.getBirthYear(),
                author.getDeathYear());
    }
}
