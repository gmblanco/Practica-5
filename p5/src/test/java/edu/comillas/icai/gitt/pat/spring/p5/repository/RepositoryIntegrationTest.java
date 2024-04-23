package edu.comillas.icai.gitt.pat.spring.p5.repository;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RepositoryIntegrationTest {
    @Autowired TokenRepository tokenRepository;
    @Autowired AppUserRepository appUserRepository;

    /**
     * TODO#9
     * Completa este test de integración para que verifique
     * que los repositorios TokenRepository y AppUserRepository guardan
     * los datos correctamente, y las consultas por AppToken y por email
     * definidas respectivamente en ellos retornan el token y usuario guardados.
     */
    @Test void saveTest() {
        // Given ...
        AppUser user = new AppUser();
        Token token = new Token();
        user.name = "pepe";
        user.email = "pepe@gmail.com";
        user.role = Role.USER;
        user.password = "Clave1234";

        token.appUser = user;

        // When ...
        appUserRepository.save(user);
        tokenRepository.save(token);
        // Then ...
        AppUser usuarioGuardado = appUserRepository.findByEmail("pepe@gmail.com");
        assertNotNull(usuarioGuardado);
        assertEquals("Clave1234",usuarioGuardado.password);
        assertEquals(Role.USER,usuarioGuardado.role);
        assertEquals("pepe",usuarioGuardado.name);

        Token tokenGuardado = tokenRepository.findByAppUser(usuarioGuardado);
        assertNotNull(tokenGuardado);
        assertEquals(user,tokenGuardado.getAppUser());
    }

    /**
     * TODO#10
     * Completa este test de integración para que verifique que
     * cuando se borra un usuario, automáticamente se borran sus tokens asociados.
     */
    @Test void deleteCascadeTest() {
        // Given ...
        AppUser user = new AppUser();
        Token token = new Token();
        user.name = "pepe";
        user.email = "pepe@gmail.com";
        user.role = Role.USER;
        user.password = "Clave1234";
        token.appUser = user;
        appUserRepository.save(user);
        tokenRepository.save(token);
        // When ...
        appUserRepository.delete(user);
        // Then ...
        assertEquals(0, appUserRepository.count());
        assertEquals(0, tokenRepository.count());
    }
}