package com.aora.apirest.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aora.apirest.entities.Users;
import com.aora.apirest.services.CustomUserDetailService;
import com.aora.apirest.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// filtro para validar el token jwt
// onceperrequestfilter se utiliza para crear filtros personalizados y
// se ejecuta una vez por cada request

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CustomUserDetailService customUserDetailService;

  // realizara todos los filtros relacionados al token jwt
  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
      // obtenemos el token de la request
      final Optional<String> token = getTokenFromRequest(request);
      final String userEmail;

      if (token.isEmpty()) {
        // si el token es nulo, se ejecuta el siguiente filtro
        // esto es necesario para que el request continue su flujo
        filterChain.doFilter(request, response);
        return;
      }

      // obtenemos el email del usuario desde el token
      userEmail = jwtService.getEmailFromToken(token.get());

      // si el email no es nulo y el contexto de seguridad no tiene una autenticación
      if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // obtenemos los detalles del usuario de la base de datos
        Users userDetails = customUserDetailService.loadUserByEmail(userEmail);

        // si el token es válido, se autentica el usuario
        if(jwtService.isTokenValid(token.get(), userDetails)) {
          // actualizamos el contexto de seguridad
          // se crea un nuevo token de autenticación
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
          // se establece el detalle del token
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // se establece el contexto de seguridad
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      // al finalizar el filtro, se ejecuta el siguiente filtro
      // esto es necesario para que el request continue su flujo
      filterChain.doFilter(request, response);
  }

  // metodo para obtener el token de la request
  private Optional<String> getTokenFromRequest(HttpServletRequest request) {
    // final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String authHeader = request.getHeader("Authorization");
    // si el header no es nulo y el header empieza con "Bearer "
    if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      // se retorna el token que esta despues de "Bearer "
      return Optional.of(authHeader.substring(7));
    }
    return Optional.empty();
  }

}
