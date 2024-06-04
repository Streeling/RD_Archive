package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

/**
 * Copied from https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html#oauth2-client-access-protected-resources
 */
@RestController
public class CustomerController {

  private final WebClient webClient;

  public CustomerController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/customers")
  public ResponseEntity<String> customers() {
    return this.webClient.get()
//        .uri("http://localhost:8080/customers")
        .uri("http://localhost:8082/customers")
        .attributes(clientRegistrationId("keycloak"))
//        .attributes(clientRegistrationId("github"))
//        .attributes(clientRegistrationId("google"))
        .retrieve()
        .toEntity(String.class)
        .block();
  }
}
