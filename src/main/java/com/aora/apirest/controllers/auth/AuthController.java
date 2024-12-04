package com.aora.apirest.controllers.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aora.apirest.dto.JwtResponse;
import com.aora.apirest.dto.LoginRequest;
import com.aora.apirest.dto.RegisterRequest;
import com.aora.apirest.entities.Users;
import com.aora.apirest.repositories.UserRepository;
import com.aora.apirest.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;
 

  // response entity es un objeto que se utiliza para devolver una respuesta http 
  // con el status code, headers, y body

  // endpoint para registrar usuarios
  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
    // se registra el usuario
    userService.registerUser(request);
    // se retorna una respuesta con el status code 200 y el mensaje "Usuario registrado con éxito"
      return ResponseEntity.ok("Usuario registrado con éxito");
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest request) {
    
   
    return ResponseEntity.ok(userService.loginUser(request));
  }

  @GetMapping("/")
public String getMethodName() {
    return "hola";
}
  
  // iniciar sesión
  // @PostMapping("/login")
  // public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest request) {
      
  //   try {
  //     // Autentica las credenciales del usuario
  //     authenticationManager.authenticate(
  //       new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
  //     );

  //     // genera un token JWT
  //     String token = jwtUtil.generateToken()
  //   } catch (Exception e) {
  //     // TODO: handle exception
  //   }
      
  //     return request;
  // }
  

  
}
