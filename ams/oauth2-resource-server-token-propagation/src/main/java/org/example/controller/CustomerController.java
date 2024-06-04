package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CustomerController {

  private WebClient webClient;

  public CustomerController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/customers")
  public ResponseEntity<String> customers() {
    return this.webClient.get()
        .uri("http://localhost:8080/customers")
        .retrieve()
        .toEntity(String.class)
        .block();
  }
}
