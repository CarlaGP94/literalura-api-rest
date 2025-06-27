-- Este script crea las tablas book y author.

CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    complete_name VARCHAR(500) NOT NULL,
    birth_year INT,
    death_year INT
);

CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(1000) NOT NULL,
    download_count INT,
    language VARCHAR(50),
    author_id BIGINT,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES author(id)
);