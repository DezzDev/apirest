package com.aora.apirest.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aora.apirest.entities.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

  public String getToken(Users user) {
    return getToken(new HashMap<>(), user);
  }

  private String getToken(Map<String, Object> extraClaims, Users user) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(user.getEmail())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getKey())
        .compact(); // crea el objeto y lo serializa
  }

  private Key getKey() {
    // pasamos la key a bytes
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  
  public String getEmailFromToken(String token) {
   return getClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, Users userDetails) {
    final String userEmail = getEmailFromToken(token);
    return (userEmail != null && userEmail.equals(userDetails.getEmail()) && !isTokenExpired(token));
  }

  // metodo para obtener todos los claims del token
  @SuppressWarnings("deprecation")
  private Claims getAllClaims(String token){
    return Jwts
        .parser()
        .setSigningKey(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // metodo para obtener un claim específico del token
  public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // metodo para obtener la fecha de expiración del token
  private Date getExpiration(String token) {
    return getClaim(token, Claims::getExpiration);
  }

  // metodo para verificar si el token ha expirado
  private boolean isTokenExpired(String token) {
    return getExpiration(token).before(new Date());
  }
}
