package com.aora.apirest.dto;

import com.aora.apirest.enums.Role;

public class RegisterRequest {
  
  private String email;
  private String password;
  private Role role;

  // Constructor vacío
  public RegisterRequest() {
  }

  // Getters y setters
  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}