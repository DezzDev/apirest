package com.aora.apirest.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/")
public class demoController {
  
  @GetMapping("/demo")
  public String welcome() {
     
      
      return "welcome to security endpoint";
  }
  

}
