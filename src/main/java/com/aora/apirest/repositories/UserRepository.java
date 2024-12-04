package com.aora.apirest.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aora.apirest.entities.Users;




// se especifica la entity y el tipo del Id, en este caso long
public interface UserRepository extends JpaRepository<Users, Long> {

  // los métodos se generan automáticamente gracias a jpa, utilizando el nombre del método 
  boolean existsByEmail(String email);

  Optional<Users> findByEmail(String email);
  
  
}
