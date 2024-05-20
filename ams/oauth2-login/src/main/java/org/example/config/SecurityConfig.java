package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * Copied from https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html#oauth2-client-log-users-in and
 * https://docs.spring.io/spring-security/reference/reactive/oauth2/login/logout.html
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final ClientRegistrationRepository clientRegistrationRepository;

  public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/info").anonymous()
            .anyRequest().authenticated()
        )
        .oauth2Login(Customizer.withDefaults())
        .logout((logout) -> logout
            .logoutSuccessHandler(oidcLogoutSuccessHandler())
        );
    return http.build();
  }

  private SimpleUrlLogoutSuccessHandler oidcLogoutSuccessHandler() {
    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
        new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

    // Sets the location that the End-User's User Agent will be redirected to
    // after the logout has been performed at the Provider
    oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8081/info");

    return oidcLogoutSuccessHandler;
  }
}