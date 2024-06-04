package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@RestController
public class InfoController {

  private final WebClient webClient;

  public InfoController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/info")
  public ResponseEntity<String> info() {
    return this.webClient.get()
//        .uri("http://localhost:8080/info")
        .uri("http://localhost:8082/info")
//        .attributes(clientRegistrationId("keycloak"))
//        .attributes(clientRegistrationId("github"))
//        .attributes(clientRegistrationId("google"))
        .retrieve()
        .toEntity(String.class)
        .block();
  }
}
