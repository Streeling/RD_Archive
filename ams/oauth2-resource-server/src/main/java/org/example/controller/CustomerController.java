package org.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

  @GetMapping("/customers")
  public String customers(Authentication auth) {
    System.out.println(((org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal()).getClaims().get("preferred_username"));
    System.out.println(auth.getAuthorities());
    return "customers";
  }
}
