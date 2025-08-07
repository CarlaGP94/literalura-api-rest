package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.domain.user.UserApp;
import com.alura.literalura_api_rest.infra.security.AuthenticationDetail;
import com.alura.literalura_api_rest.infra.security.TokenJWTDetail;
import com.alura.literalura_api_rest.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<AuthenticationDetail> jsonAuthenticationDetail;

    @Autowired
    private JacksonTester<TokenJWTDetail> jsonTokenJWTDetail;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("Debe devolver status 200 cuando los datos de autenticación son válidos")
    void userLogin_scenery1() throws Exception {
        // ARRANGE
        var login = "testuser";
        var password = "123456";
        var tokenEsperado = "abc.123.xyz";

        AuthenticationDetail datosAuth = new AuthenticationDetail(login, password);

        // Creamos un objeto UserApp simulado que es lo que debería devolver
        UserApp mockUserApp = new UserApp(login); // Crea un UserApp con el login

        // Creamos un objeto Authentication simulado (un mock)
        Authentication authentication = mock(Authentication.class);

        // Configuramos el mock para que getPrincipal() devuelva nuestro mock de UserApp
        when(authentication.getPrincipal()).thenReturn(mockUserApp);

        // Configuramos el AuthenticationManager para que devuelva nuestro objeto Authentication simulado
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Configuramos el TokenService para que reciba nuestro mock de UserApp
        when(tokenService.createToken(mockUserApp)).thenReturn(tokenEsperado);

        // ACT
        var response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAuthenticationDetail.write(datosAuth).getJson())
        ).andReturn().getResponse();

        // ASSERT
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonTokenJWTDetail.write(new TokenJWTDetail(tokenEsperado)).getJson());
    }
}
