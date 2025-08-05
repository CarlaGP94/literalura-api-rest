package com.alura.literalura_api_rest.infra.security;

public record AuthenticationDetail(
        String login,
        String password
) {
}
