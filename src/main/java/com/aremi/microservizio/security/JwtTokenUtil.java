package com.aremi.microservizio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/***
 * JwtTokenUtil è una classe personalizzata responsabile della generazione, validazione, e dell’estrazione di informazioni dai token JWT.
 */
@Component
public class JwtTokenUtil {

    /***
     *  * Questo attributo contiene il segreto utilizzato per firmare i token JWT. Il valore viene iniettato da un file di proprietà esterno (ad esempio,
     *  application.properties).
     */
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token.validity}")
    private long JWT_TOKEN_VALIDITY;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /***
     * Questo metodo estrae un claim specifico dal token JWT. Un claim è una dichiarazione fatta dal soggetto (in questo caso, l’utente) che può essere
     * utilizzata per trasportare informazioni extra nel token.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /***
     * Questo metodo estrae tutti i claim dal token JWT.
     */
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /***
     * Questo metodo effettivamente costruisce il token JWT.
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key).compact();
    }

    /***
     * Questo metodo valida un token JWT. Controlla se il nome utente nel token corrisponde al nome utente fornito e se il token è scaduto.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
