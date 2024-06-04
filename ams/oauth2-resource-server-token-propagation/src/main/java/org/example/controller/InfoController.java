package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class InfoController {

  private WebClient webClient;

  public InfoController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/info")
  public ResponseEntity<String> info() {
    return this.webClient.get()
        .uri("http://localhost:8080/info")
        .retrieve()
        .toEntity(String.class)
        .block();
  }
}
