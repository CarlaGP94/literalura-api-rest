package com.alura.literalura_api_rest.domain.external.api_gutendex;

public interface IGutendexConverter {
    <T> T getData(String json, Class<T> clase);
}
