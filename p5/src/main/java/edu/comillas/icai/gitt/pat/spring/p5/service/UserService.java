package edu.comillas.icai.gitt.pat.spring.p5.service;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import edu.comillas.icai.gitt.pat.spring.p5.repository.TokenRepository;
import edu.comillas.icai.gitt.pat.spring.p5.repository.AppUserRepository;
import edu.comillas.icai.gitt.pat.spring.p5.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/*
 * TODO#6
 * Completa los métodos del servicio para que cumplan con el contrato
 * especificado en el interface UserServiceInterface, utilizando
 * los repositorios y entidades creados anteriormente
 */

@Service
public class UserService implements UserServiceInterface {
    @Autowired
    AppUserRepository repoAppUser;
    @Autowired
    TokenRepository repoToken;
    @Autowired
    Hashing hashing;

    public Token login(String email, String password) {
        AppUser appUser = repoAppUser.findByEmail(email);
        if (appUser == null || !hashing.compare(appUser.password,password)) return null; //si las credenciales son incorrectas, retorna null

        Token token = repoToken.findByAppUser(appUser); //obtiene el token de sesion asociado a dicho usuario
        if (token != null) return token;//lo devuelve

        token = new Token();
        token.appUser = appUser;
        repoToken.save(token);
        /*Cuando un usuario inicia sesión por primera vez o cuando no tiene un token de sesión activo
        (por ejemplo, después de eliminar su token anterior o si es la primera vez que inicia sesión),
        creamos un nuevo token de sesión actual.*/
        return token;
    }

    public AppUser authentication(String tokenId) {
        Token token = repoToken.findById(tokenId).orElse(null);

        if (token != null) {
            return token.getAppUser(); // Si se encontró el token, devuelve el usuario asociado
        } else {
            return null; // Si el token no se encuentra, retornamos null
        }
    }

    public ProfileResponse profile(AppUser appUser) {
        return new ProfileResponse(appUser.name, appUser.email, appUser.role);
    }

    /**
     * @param appUser usuario
     * @param profile nuevos datos para el perfil del usuario
     * @return respuesta con el perfil de dicho usuario actualizado
     */
    public ProfileResponse profile(AppUser appUser, ProfileRequest profile) {

        appUser.name = profile.name();
        appUser.role = profile.role();
        appUser.password = hashing.hash(profile.password());

        repoAppUser.save(appUser);//guardamos los cambios actualizados en bases de datos

        return new ProfileResponse(appUser.name, appUser.email, appUser.role);
    }

    public ProfileResponse profile(RegisterRequest register) {
        AppUser appUser = new AppUser();
        appUser.name = register.name();
        appUser.email = register.email();
        appUser.role = register.role();
        appUser.password = hashing.hash(register.password());
        repoAppUser.save(appUser);
        return new ProfileResponse(appUser.name, appUser.email, appUser.role);
    }

    public void logout(String tokenId) {
        Token token = repoToken.findById(tokenId).orElse(null);
        if (token != null) {
            repoToken.delete(token);
        }
    }

    @Override
    public void delete(AppUser appUser) {
        repoAppUser.delete(appUser);
    }

}
