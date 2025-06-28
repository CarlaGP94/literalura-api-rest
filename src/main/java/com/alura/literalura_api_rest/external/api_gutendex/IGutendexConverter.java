package com.alura.literalura_api_rest.external.api_gutendex;

public interface IGutendexConverter {
    <T> T getData(String json, Class<T> clase);
}
