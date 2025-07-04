package com.alura.literalura_api_rest.configuration.security;

import com.alura.literalura_api_rest.repository.IUserAppRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private IUserAppRepository userAppRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recoverToken(request);

        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); // obtengo el nombre de usuario
            var myUser = userAppRepository.findByLogin(subject); // busco si está en el repositorio

            // lo valido "manualmente"
            var authentication = new UsernamePasswordAuthenticationToken(myUser, null, myUser.getAuthorities());

            // le digo a spring que está ok y le de acceso.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }

    private String recoverToken(HttpServletRequest request) {
        // Recupera el Token dentro del header authorization.
        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ","");
        }

        return null;
    }

}
