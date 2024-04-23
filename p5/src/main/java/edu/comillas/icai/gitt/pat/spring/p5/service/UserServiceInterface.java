package edu.comillas.icai.gitt.pat.spring.p5.service;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceInterface {

    /**
     * @param email email proporcionado para el login
     * @param password password proporcionado para el login
     * @return si las credenciales del usuario son correctas, retorna un token de sesión asociado a dicho usuario;
     * si las credenciales son incorrectas, retorna null
     */
    Token login(String email, String password);

    /**
     * @param tokenId token de la sesión actual del usuario
     * @return si la sesión está creada (el token existe en BD), retorna el usuario asociado a dicha sesión;
     * si la sesión no existe, retorna null
     */
    AppUser authentication(String tokenId);

    /**
     * @param appUser usuario
     * @return respuesta con el perfil de dicho usuario
     */
    ProfileResponse profile(AppUser appUser);

    /**
     * @param appUser usuario
     * @param profile nuevos datos para el perfil del usuario
     * @return respuesta con el perfil de dicho usuario actualizado
     */
    ProfileResponse profile(AppUser appUser, ProfileRequest profile);

    /**
     *
     * @param register datos de registro del usuario
     * @return respuesta con el perfil del nuevo usuario
     */
    ProfileResponse profile(RegisterRequest register);

    /**
     * @param tokenId token de la sesión actual del usuario para cerrarla
     */
    void logout(String tokenId);

    /**
     * @param appUser usuario a borrar o dar de baja definitivamente
     */
    void delete(AppUser appUser);

}
