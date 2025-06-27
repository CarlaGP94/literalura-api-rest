package com.alura.literalura_api_rest.service.api_gutendex;

public interface IConvertsData {
    <T> T getData(String json, Class<T> clase);
}
