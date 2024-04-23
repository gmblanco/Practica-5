package edu.comillas.icai.gitt.pat.spring.p5.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * TODO#14
 * Utiliza esta clase para guardar en BD y comparar los passwords de forma cifrada:
 * <a href="https://en.wikipedia.org/wiki/Cryptographic_hash_function#Password_verification">Password verification</a>
 * Para ello modifica las partes necesarias en el código creado anteriormente en UserService
 */
@Component
public class Hashing {

    /**
     * @param string cadena a hashear
     * @return cadena hasheada
     */
    public String hash(String string) {
        return hash(string, generateSalt());
    }

    /**
     * @param hashedString cadena hasheada
     * @param string cadena sin hashear
     * @return true si el hash de string es igual a hashedString
     */
    public boolean compare(String hashedString, String string) {
        String[] hashParts = hashedString.split(":");
        byte[] salt = Base64.getDecoder().decode(hashParts[0]);
        return hashedString.equals(hash(string, salt));
    }

    private String hash(String string, byte[] salt) throws ResponseStatusException {
        try {
            KeySpec spec = new PBEKeySpec(string.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server not properly configured", e);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
