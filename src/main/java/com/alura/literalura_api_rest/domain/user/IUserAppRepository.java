package com.alura.literalura_api_rest.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserAppRepository extends JpaRepository<UserApp, Long> {

    UserDetails findByLogin(String login);
}
