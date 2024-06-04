package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Copied from https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html#oauth2-client-access-protected-resources
 */
@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        // See // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/bearer-tokens.html#_bearer_token_propagation
        .filter(new ServletBearerExchangeFilterFunction())
        .build();
  }
}