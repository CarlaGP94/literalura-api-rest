package com.alura.literalura_api_rest.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class IUserRepositoryTest {

    @Autowired
    private IUserAppRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Crear y guardar un usuario de prueba para los tests
        UserApp testUser = new UserApp();
        testUser.setLogin("testuser");
        testUser.setPassword("encrypted_password_here");
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("Debe retornar el usuario correcto cuando el login existe en la base de datos")
    void findByLogin_scenery1(){
        // ARRANGE
        String existingLogin = "testuser";

        // ACT
        UserDetails foundUser = userRepository.findByLogin(existingLogin);

        // ASSERT
        assertNotNull(foundUser, "El usuario encontrado no debe ser nulo");
        assertEquals(existingLogin, foundUser.getUsername(), "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Debe retornar nulo cuando el login no existe en la base de datos")
    void findByLogin_scenery2(){
        // ARRANGE
        String nonExistentLogin = "nonexistentuser";

        // ACT
        UserDetails foundUser = userRepository.findByLogin(nonExistentLogin);

        // ASSERT
        assertNull(foundUser, "El usuario encontrado debe ser nulo para un login inexistente");
    }
}