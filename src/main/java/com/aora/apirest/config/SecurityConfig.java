package com.aora.apirest.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*")); // permite todas las origenes
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*")); // permite todos los headers
    configuration.setExposedHeaders(Arrays.asList("Authorization")); // expone el header Authorization
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // aplica la configuración a todas las rutas
    return source;
  }
}
