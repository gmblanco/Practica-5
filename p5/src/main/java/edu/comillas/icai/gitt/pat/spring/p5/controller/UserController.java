package edu.comillas.icai.gitt.pat.spring.p5.controller;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.LoginRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import edu.comillas.icai.gitt.pat.spring.p5.service.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {
    @Autowired UserServiceInterface userService;

    @PostMapping("/api/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody RegisterRequest register) {
        try {
            return userService.profile(register);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PostMapping("/api/users/me/session")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest credentials) {
        Token token = userService.login(credentials.email(), credentials.password());
        if (token == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        ResponseCookie session = ResponseCookie
                .from("session", token.id)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, session.toString()).build();
    }

    @DeleteMapping("/api/users/me/session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> logout(@CookieValue(value = "session", required = true) String session) {
        AppUser appUser = userService.authentication(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        userService.logout(session);
        ResponseCookie expireSession = ResponseCookie
                .from("session")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).header(HttpHeaders.SET_COOKIE, expireSession.toString()).build();
    }

    @GetMapping("/api/users/me")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse profile(@CookieValue(value = "session", required = true) String session) {
        AppUser appUser = userService.authentication(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.profile(appUser);
    }

    @PutMapping("/api/users/me")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse update(@RequestBody ProfileRequest profile, @CookieValue(value = "session", required = true) String session) {
        AppUser appUser = userService.authentication(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.profile(appUser, profile);
    }

    @DeleteMapping("/api/users/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@CookieValue(value = "session", required = true) String session) {
        AppUser appUser = userService.authentication(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        userService.delete(appUser);
    }


}

