package com.aora.apirest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aora.apirest.dto.JwtResponse;
import com.aora.apirest.dto.LoginRequest;
import com.aora.apirest.dto.RegisterRequest;
import com.aora.apirest.entities.Users;
import com.aora.apirest.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;


  public void registerUser(RegisterRequest request) {
    // verifica si el email o username ya existen
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("El correo ya existe");
    }
    if (request.getPassword().isEmpty()) {
      throw new RuntimeException("La contraseña no puede estar vacía");
    }

    // crea un nuevo usuario con la contraseña encriptada
    Users user = new Users();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode((request.getPassword())));
    user.setRole(request.getRole());

    userRepository.save(user);

    // String token = jwtService.getToken(user);
    // return new JwtResponse(token);
  }

  public JwtResponse loginUser(LoginRequest request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    Users user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Email no encontrado"));
    
    String token = jwtService.getToken(user);

    JwtResponse jwtResponse = new JwtResponse(token);
    return jwtResponse;
  }

  // public Users findByUsername(String username){
  // return userRepository.findByUsername(username)
  // .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
  // }

}
