package com.alura.literalura_api_rest.controller;

import com.alura.literalura_api_rest.dto.AuthenticationDetail;
import com.alura.literalura_api_rest.dto.TokenJWTDetail;
import com.alura.literalura_api_rest.model.UserApp;
import com.alura.literalura_api_rest.configuration.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity userLogin(@RequestBody @Valid AuthenticationDetail data){
        // Conversor: de tipo DTO a tipo Authentication, para poder usar el manager.
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(),data.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.createToken((UserApp) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWTDetail(tokenJWT));
    }


}
