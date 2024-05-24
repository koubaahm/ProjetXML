package fr.univrouen.ProjetXML.services;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.util.function.Function;

public interface JwtServiceInterface {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    long getExpirationTime();
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenValid(String token);
    void invalidateToken(String token);
    void addValidToken(String token);
    String extractToken(HttpServletRequest request);
}
