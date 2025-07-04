-- Este script crea la tabla del usuario.

CREATE TABLE user_app (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);