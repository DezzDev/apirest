package com.aora.apirest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aora.apirest.jwt.JwtAuthenticationFilter;

// La anotación @Configuration indica que esta clase contiene definiciones de beans.
// @Bean le dice a Spring que debe gestionar la instancia de PasswordEncoder.
// new BCryptPasswordEncoder() crea una instancia del codificador de contraseñas basado en el algoritmo bcrypt.

// configuramos para que no se guarde la sesión en el servidor ya que usaremos JWT

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Autowired
  private JwtAuthenticationFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // toda ruta que empiece por /auth se permite
    return http
      .csrf(csrf -> csrf.disable()) // deshabilitar csrf
      .authorizeHttpRequests(authRequest ->
        authRequest
          .requestMatchers("/auth/**").permitAll() // permite todas las rutas que empiezan por /auth
          .anyRequest().authenticated() // cualquier otra ruta requiere autenticación
      )//.formLogin(withDefaults()) // configuracion por defecto del login
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// no se guarda la sesión en el servidor
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
 
}
