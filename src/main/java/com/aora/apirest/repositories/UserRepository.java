package com.aora.apirest.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aora.apirest.entities.User;

// se especifica la entity y el tipo del Id, en este caso long
public interface UserRepository extends JpaRepository<User, Long> {
  
}
